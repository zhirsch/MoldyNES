package com.zacharyhirsch.moldynes.emulator.io;

import static com.zacharyhirsch.jna.sdl3.SDL_h.*;

import com.google.common.collect.ImmutableMap;
import com.zacharyhirsch.jna.sdl3.SDL_AudioSpec;
import com.zacharyhirsch.jna.sdl3.SDL_Event;
import com.zacharyhirsch.jna.sdl3.SDL_KeyboardEvent;
import java.io.Closeable;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.lang.foreign.ValueLayout;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;

public final class SdlDisplay implements Closeable, Display {
  
  private static final int W = 256;
  private static final int H = 240;
  private static final int SCALE = 3;
  private static final double FPS = 60.0988;
  private static final Duration FRAME_DURATION = Duration.ofNanos((long) (1_000_000_000 / FPS));
  private static final int CPU_CYCLES_PER_SECOND = (int) (341 * 262 * FPS / 3);

  private static final Map<Integer, NesJoypad.Button> JOYPAD1_KEYS =
      ImmutableMap.<Integer, NesJoypad.Button>builder()
          .put(SDLK_S(), NesJoypad.Button.DOWN)
          .put(SDLK_W(), NesJoypad.Button.UP)
          .put(SDLK_D(), NesJoypad.Button.RIGHT)
          .put(SDLK_A(), NesJoypad.Button.LEFT)
          .put(SDLK_Z(), NesJoypad.Button.SELECT)
          .put(SDLK_X(), NesJoypad.Button.START)
          .put(SDLK_M(), NesJoypad.Button.BUTTON_A)
          .put((int) ',', NesJoypad.Button.BUTTON_B)
          .build();

  private static final Map<Integer, NesJoypad.Button> JOYPAD2_KEYS =
      ImmutableMap.<Integer, NesJoypad.Button>builder()
          .put(SDLK_DOWN(), NesJoypad.Button.DOWN)
          .put(SDLK_UP(), NesJoypad.Button.UP)
          .put(SDLK_RIGHT(), NesJoypad.Button.RIGHT)
          .put(SDLK_LEFT(), NesJoypad.Button.LEFT)
          .put(SDLK_SPACE(), NesJoypad.Button.SELECT)
          .put(SDLK_RETURN(), NesJoypad.Button.START)
          .put(SDLK_PERIOD(), NesJoypad.Button.BUTTON_A)
          .put(SDLK_SLASH(), NesJoypad.Button.BUTTON_B)
          .build();

  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final MemorySegment window;
  private final MemorySegment renderer;
  private final MemorySegment texture;

  private final MemorySegment audioBuffer;
  private int audioBufferLen;
  private final int audioDeviceId;
  private final MemorySegment audioStream;

  private Instant lastFrameTime = null;
  public boolean quit = false;

  public SdlDisplay(NesJoypad joypad1, NesJoypad joypad2) {
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;

    if (!SDL_InitSubSystem(SDL_INIT_VIDEO())) {
      throw new SdlException("Unable to initialize SDL video library");
    }

    MemorySegment title = Arena.global().allocateFrom("MoldyNES");
    MemorySegment windowPtr = Arena.global().allocate(C_POINTER);
    MemorySegment rendererPtr = Arena.global().allocate(C_POINTER);
    if (!SDL_CreateWindowAndRenderer(title, W * SCALE, H * SCALE, 0, windowPtr, rendererPtr)) {
      throw new SdlException("Unable to create SDL window and renderer");
    }
    window = windowPtr.get(C_POINTER, 0);
    renderer = rendererPtr.get(C_POINTER, 0);
    if (!SDL_SetRenderLogicalPresentation(renderer, W, H, SDL_LOGICAL_PRESENTATION_DISABLED())) {
      throw new SdlException("Unable to set logical size");
    }
    if (!SDL_SetRenderDrawColor(renderer, (byte) 0, (byte) 255, (byte) 0, (byte) 255)) {
      throw new SdlException("Unable to set draw color");
    }
    texture =
        SDL_CreateTexture(renderer, SDL_PIXELFORMAT_RGB24(), SDL_TEXTUREACCESS_STREAMING(), W, H);
    if (texture == null) {
      throw new SdlException("Unable to create SDL texture");
    }

    if (!SDL_InitSubSystem(SDL_INIT_AUDIO())) {
      throw new SdlException("Unable to initialize SDL audio library");
    }

    audioBuffer = Arena.global().allocate(1 << 20);
    audioBufferLen = 0;

    try (Arena arena = Arena.ofConfined()) {
      MemorySegment output = SDL_AudioSpec.allocate(arena);
      SDL_AudioSpec.freq(output, 44100);
      SDL_AudioSpec.format(output, SDL_AUDIO_F32());
      SDL_AudioSpec.channels(output, 1);
      audioDeviceId = SDL_OpenAudioDevice(SDL_AUDIO_DEVICE_DEFAULT_PLAYBACK(), output);
      if (audioDeviceId == 0) {
        throw new SdlException("Unable to open audio device");
      }

      MemorySegment input = SDL_AudioSpec.allocate(arena);
      SDL_AudioSpec.freq(input, CPU_CYCLES_PER_SECOND);
      SDL_AudioSpec.format(input, SDL_AUDIO_F32());
      SDL_AudioSpec.channels(input, 1);
      audioStream = SDL_CreateAudioStream(input, output);
      if (audioStream == null) {
        throw new SdlException("Unable to open audio stream");
      }
      if (!SDL_BindAudioStream(audioDeviceId, audioStream)) {
        throw new SdlException("Unable to bind audio stream");
      }
      if (!SDL_ResumeAudioDevice(audioDeviceId)) {
        throw new SdlException("Unable to resume audio device");
      }
    }
  }

  @Override
  public void draw(byte[] frame) {
    delay();
    pump();
    outputGraphics(frame);
    outputAudio();
  }

  @Override
  public void play(float sample) {
    audioBuffer.set(ValueLayout.JAVA_FLOAT, audioBufferLen, sample);
    audioBufferLen += Float.BYTES;
  }

  public void setError() {
    if (!SDL_SetRenderDrawColor(renderer, (byte) 255, (byte) 0, (byte) 0, (byte) 255)) {
      throw new SdlException("Unable to set draw color");
    }
    if (!SDL_RenderRect(renderer, null)) {
      throw new SdlException("Unable to draw rectangle");
    }
    if (!SDL_RenderPresent(renderer)) {
      throw new SdlException("Unable to present renderer");
    }
  }

  private void delay() {
    if (lastFrameTime != null) {
      Duration elapsed = Duration.between(lastFrameTime, Instant.now());
      Duration delay = FRAME_DURATION.minus(elapsed);
      if (delay.isPositive()) {
        SDL_DelayNS(delay.toNanos());
      }
    }
    lastFrameTime = Instant.now();
  }

  public void pump() {
    try (Arena arena = Arena.ofConfined()) {
      MemorySegment event = SDL_Event.allocate(arena);
      while (SDL_PollEvent(event)) {
        dispatch(event);
      }
    }
  }

  private void outputGraphics(byte[] frame) {
    try (Arena arena = Arena.ofConfined()) {
      MemorySegment pixelsPtr = arena.allocate(C_POINTER);
      MemorySegment pitch = arena.allocate(C_POINTER);
      if (!SDL_LockTexture(texture, MemorySegment.NULL, pixelsPtr, pitch)) {
        throw new SdlException("Unable to lock texture");
      }
      MemorySegment pixels = pixelsPtr.get(C_POINTER, 0);
      MemorySegment.copy(frame, 0, pixels, ValueLayout.JAVA_BYTE, 0, frame.length);
      SDL_UnlockTexture(texture);
    }
    if (!SDL_RenderTexture(renderer, texture, MemorySegment.NULL, MemorySegment.NULL)) {
      throw new SdlException("Unable to render texture");
    }
    if (!SDL_RenderPresent(renderer)) {
      throw new SdlException("Unable to present renderer");
    }
  }

  private void outputAudio() {
    if (audioBufferLen == 0) {
      return;
    }
    if (!SDL_PutAudioStreamData(audioStream, audioBuffer, audioBufferLen)) {
      throw new SdlException("Unable to add sample to audio stream");
    }
    audioBufferLen = 0;
  }

  private void dispatch(MemorySegment event) {
    int type = SDL_Event.type(event);
    if (type == SDL_EVENT_QUIT()) {
      quit = true;
    } else if (type == SDL_EVENT_KEY_DOWN()) {
      onKeyPress(SDL_Event.key(event), true);
    } else if (type == SDL_EVENT_KEY_UP()) {
      onKeyPress(SDL_Event.key(event), false);
    }
  }

  private void onKeyPress(MemorySegment key, boolean down) {
    int keycode = SDL_KeyboardEvent.key(key);
    NesJoypad.Button button1 = JOYPAD1_KEYS.get(keycode);
    if (button1 != null) {
      joypad1.setButton(button1, down);
    }
    NesJoypad.Button button2 = JOYPAD2_KEYS.get(keycode);
    if (button2 != null) {
      joypad2.setButton(button2, down);
    }
  }

  @Override
  public void close() {
    SDL_DestroyRenderer(renderer);
    SDL_DestroyWindow(window);
    SDL_CloseAudioDevice(audioDeviceId);
    SDL_DestroyAudioStream(audioStream);
    SDL_Quit();
  }

  private static final class SdlException extends RuntimeException {

    SdlException(String message) {
      super("%s: %s".formatted(message, SDL_GetError().getString(0)));
    }
  }
}
