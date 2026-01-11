package com.zacharyhirsch.moldynes.emulator.io;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_AudioStreamAvailable;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_AudioStreamGet;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_AudioStreamPut;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_CloseAudioDevice;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_FreeAudioStream;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_NewAudioStream;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_OpenAudioDevice;
import static io.github.libsdl4j.api.audio.SdlAudio.SDL_PauseAudioDevice;
import static io.github.libsdl4j.api.audio.SdlAudioConst.AUDIO_F32SYS;
import static io.github.libsdl4j.api.audio.SdlAudioConst.SDL_AUDIO_ALLOW_ANY_CHANGE;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYUP;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_A;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_D;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_DOWN;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_LEFT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_M;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_PERIOD;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_RETURN;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_RIGHT;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_S;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_SLASH;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_SPACE;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_UP;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_W;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_X;
import static io.github.libsdl4j.api.keycode.SDL_Keycode.SDLK_Z;
import static io.github.libsdl4j.api.pixels.SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24;
import static io.github.libsdl4j.api.render.SDL_RendererFlags.SDL_RENDERER_ACCELERATED;
import static io.github.libsdl4j.api.render.SDL_TextureAccess.SDL_TEXTUREACCESS_STREAMING;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateRenderer;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateTexture;
import static io.github.libsdl4j.api.render.SdlRender.SDL_DestroyRenderer;
import static io.github.libsdl4j.api.render.SdlRender.SDL_LockTexture;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderCopy;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderDrawRect;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderPresent;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderSetLogicalSize;
import static io.github.libsdl4j.api.render.SdlRender.SDL_SetRenderDrawColor;
import static io.github.libsdl4j.api.render.SdlRender.SDL_UnlockTexture;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_CreateWindow;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_DestroyWindow;
import static io.github.libsdl4j.api.video.SdlVideoConst.SDL_WINDOWPOS_CENTERED;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Floats;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import com.zacharyhirsch.moldynes.emulator.NesClock;
import io.github.libsdl4j.api.audio.SDL_AudioDeviceID;
import io.github.libsdl4j.api.audio.SDL_AudioFormat;
import io.github.libsdl4j.api.audio.SDL_AudioSpec;
import io.github.libsdl4j.api.audio.SDL_AudioStream;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.events.SDL_KeyboardEvent;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_Texture;
import io.github.libsdl4j.api.video.SDL_Window;
import java.io.Closeable;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SdlDisplay implements Closeable, Display {

  private static final Logger log = LoggerFactory.getLogger(SdlDisplay.class);

  private static final int W = 256;
  private static final int H = 240;
  private static final int SCALE = 3;
  private static final double FPS = 60.0988;
  private static final Duration FRAME_DURATION = Duration.ofNanos((long) (1_000_000_000 / FPS));
  private static final int CPU_CYCLES_PER_SECOND = (int) (341 * 262 * FPS / 3);

  private static final Map<Integer, NesJoypad.Button> JOYPAD1_KEYS =
      ImmutableMap.<Integer, NesJoypad.Button>builder()
          .put(SDLK_S, NesJoypad.Button.DOWN)
          .put(SDLK_W, NesJoypad.Button.UP)
          .put(SDLK_D, NesJoypad.Button.RIGHT)
          .put(SDLK_A, NesJoypad.Button.LEFT)
          .put(SDLK_Z, NesJoypad.Button.SELECT)
          .put(SDLK_X, NesJoypad.Button.START)
          .put(SDLK_M, NesJoypad.Button.BUTTON_A)
          .put((int) ',', NesJoypad.Button.BUTTON_B)
          .build();

  private static final Map<Integer, NesJoypad.Button> JOYPAD2_KEYS =
      ImmutableMap.<Integer, NesJoypad.Button>builder()
          .put(SDLK_DOWN, NesJoypad.Button.DOWN)
          .put(SDLK_UP, NesJoypad.Button.UP)
          .put(SDLK_RIGHT, NesJoypad.Button.RIGHT)
          .put(SDLK_LEFT, NesJoypad.Button.LEFT)
          .put(SDLK_SPACE, NesJoypad.Button.SELECT)
          .put(SDLK_RETURN, NesJoypad.Button.START)
          .put(SDLK_PERIOD, NesJoypad.Button.BUTTON_A)
          .put(SDLK_SLASH, NesJoypad.Button.BUTTON_B)
          .build();

  private final NesClock clock;
  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final SDL_Window window;
  private final SDL_Renderer renderer;
  private final SDL_Texture texture;

  private final ArrayList<Float> audioBuffer;
  private final SDL_AudioDeviceID audioDeviceId;
  private final SDL_AudioStream audioStream;

  private long lastFrameCycle = 0;
  private Instant lastFrameTime = null;
  public boolean quit = false;

  public SdlDisplay(NesClock clock, NesJoypad joypad1, NesJoypad joypad2) {
    this.clock = clock;
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;

    if (SDL_Init(SDL_INIT_EVERYTHING) != 0) {
      throw new IllegalStateException("Unable to initialize SDL library: " + SDL_GetError());
    }

    window = SDL_CreateWindow("MoldyNES", SDL_WINDOWPOS_CENTERED, SDL_WINDOWPOS_CENTERED, W * SCALE, H * SCALE, 0);
    if (window == null) {
      throw new IllegalStateException("Unable to create window: " + SDL_GetError());
    }
    renderer = SDL_CreateRenderer(window, -1, SDL_RENDERER_ACCELERATED);
    if (renderer == null) {
      throw new IllegalStateException("Unable to create renderer: " + SDL_GetError());
    }
    if (SDL_RenderSetLogicalSize(renderer, W, H) != 0) {
      throw new IllegalStateException("Unable to set logical size: " + SDL_GetError());
    }
    if (SDL_SetRenderDrawColor(renderer, (byte) 0x00, (byte) 0xff, (byte) 0x00, (byte) 0xff) != 0) {
      throw new IllegalStateException("Unable to set draw color: " + SDL_GetError());
    }
    texture = SDL_CreateTexture(renderer, SDL_PIXELFORMAT_RGB24, SDL_TEXTUREACCESS_STREAMING, W, H);
    if (texture == null) {
      throw new IllegalStateException("Unable to create SDL texture: " + SDL_GetError());
    }

    audioBuffer = new ArrayList<>();

    SDL_AudioSpec desired = new SDL_AudioSpec();
    desired.freq = 44_100;
    desired.format = new SDL_AudioFormat(AUDIO_F32SYS);
    desired.channels = 1;
    desired.samples = 1024;
    desired.callback = this::playbackAudio;
    desired.userdata = null;
    SDL_AudioSpec obtained = new SDL_AudioSpec();
    audioDeviceId = SDL_OpenAudioDevice(null, 0, desired, obtained, SDL_AUDIO_ALLOW_ANY_CHANGE);
    if (audioDeviceId == null || audioDeviceId.intValue() == 0) {
      throw new IllegalStateException("Unable to open audio device: " + SDL_GetError());
    }
    SDL_PauseAudioDevice(audioDeviceId, 0);

    log.info(
        "Audio device opened with freq:{}, format:{}, samples:{}",
        obtained.freq,
        "%02x".formatted(obtained.format.shortValue()),
        obtained.samples);

    audioStream =
        SDL_NewAudioStream(
            desired.format,
            desired.channels,
            CPU_CYCLES_PER_SECOND,
            obtained.format,
            obtained.channels,
            obtained.freq);
    if (audioStream == null) {
      throw new IllegalStateException("Unable to open audio stream: " + SDL_GetError());
    }
  }

  private void playbackAudio(Pointer userdata, Pointer stream, int len) {
    int available = SDL_AudioStreamAvailable(audioStream);
    // if (available < len) {
    //   log.warn("Audio stream is lagging behind (have:{}, want:{})", available, len);
    // }
    if (SDL_AudioStreamGet(audioStream, stream, len) == -1) {
      throw new IllegalStateException("Unable to read audio stream: " + SDL_GetError());
    }
    for (int i = 0; i < len - available; i++) {
      stream.setByte(available + i, (byte) 0);
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
    audioBuffer.add(sample);
  }

  public void setError() {
    if (SDL_SetRenderDrawColor(renderer, (byte) 0xff, (byte) 0x00, (byte) 0x00, (byte) 0xff) != 0) {
      throw new IllegalStateException("Unable to set draw color: " + SDL_GetError());
    }
    if (SDL_RenderDrawRect(renderer, null) != 0) {
      throw new IllegalStateException("Unable to draw rectangle: " + SDL_GetError());
    }
    SDL_RenderPresent(renderer);
  }

  private void delay() {
    if (lastFrameTime != null && lastFrameCycle != 0) {
      Duration elapsed = Duration.between(lastFrameTime, Instant.now());
      try {
        Thread.sleep(FRAME_DURATION.minus(elapsed));
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    lastFrameTime = Instant.now();
    lastFrameCycle = clock.getCycle();
  }

  public void pump() {
    SDL_Event event = new SDL_Event();
    while (SDL_PollEvent(event) == 1) {
      dispatch(event);
    }
  }

  private void outputGraphics(byte[] frame) {
    var pixels = new PointerByReference();
    if (SDL_LockTexture(texture, null, pixels, new IntByReference()) != 0) {
      throw new IllegalStateException("Unable to lock texture: " + SDL_GetError());
    }
    pixels.getValue().write(0, frame, 0, frame.length);
    SDL_UnlockTexture(texture);
    if (SDL_RenderCopy(renderer, texture, null, null) != 0) {
      throw new IllegalStateException("Unable to render texture: " + SDL_GetError());
    }
    SDL_RenderPresent(renderer);
  }

  private void outputAudio() {
    float[] samples = Floats.toArray(audioBuffer);
    audioBuffer.clear();
    if (samples.length == 0) {
      return;
    }
    int bytes = Float.BYTES * samples.length;
    try (Memory ptr = new Memory(bytes)) {
      ptr.write(0, samples, 0, samples.length);
      if (SDL_AudioStreamPut(audioStream, ptr, bytes) != 0) {
        throw new IllegalStateException("Unable to add sample to audio stream: " + SDL_GetError());
      }
    }
  }

  private void dispatch(SDL_Event event) {
    switch (event.type) {
      case SDL_QUIT -> quit = true;
      case SDL_KEYDOWN -> onKeyPress(event.key, true);
      case SDL_KEYUP -> onKeyPress(event.key, false);
    }
  }

  private void onKeyPress(SDL_KeyboardEvent key, boolean down) {
    NesJoypad.Button button1 = JOYPAD1_KEYS.get(key.keysym.sym);
    if (button1 != null) {
      joypad1.setButton(button1, down);
    }
    NesJoypad.Button button2 = JOYPAD2_KEYS.get(key.keysym.sym);
    if (button2 != null) {
      joypad2.setButton(button2, down);
    }
  }

  @Override
  public void close() {
    if (renderer != null) {
      SDL_DestroyRenderer(renderer);
    }
    if (window != null) {
      SDL_DestroyWindow(window);
    }
    if (audioDeviceId != null) {
      SDL_CloseAudioDevice(audioDeviceId);
    }
    if (audioStream != null) {
      SDL_FreeAudioStream(audioStream);
    }
    SDL_Quit();
  }
}
