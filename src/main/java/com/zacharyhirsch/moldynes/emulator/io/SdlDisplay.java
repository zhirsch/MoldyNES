package com.zacharyhirsch.moldynes.emulator.io;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYDOWN;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_KEYUP;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_HasEvent;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PumpEvents;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_WaitEventTimeout;
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
import static io.github.libsdl4j.api.render.SDL_TextureAccess.SDL_TEXTUREACCESS_STATIC;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateTexture;
import static io.github.libsdl4j.api.render.SdlRender.SDL_CreateWindowAndRenderer;
import static io.github.libsdl4j.api.render.SdlRender.SDL_DestroyRenderer;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderClear;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderCopy;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderPresent;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderSetLogicalSize;
import static io.github.libsdl4j.api.render.SdlRender.SDL_RenderSetVSync;
import static io.github.libsdl4j.api.render.SdlRender.SDL_SetRenderDrawColor;
import static io.github.libsdl4j.api.render.SdlRender.SDL_UpdateTexture;
import static io.github.libsdl4j.api.video.SdlVideo.SDL_DestroyWindow;

import com.google.common.collect.ImmutableMap;
import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.zacharyhirsch.util.CircularCounter;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.event.events.SDL_KeyboardEvent;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_Texture;
import io.github.libsdl4j.api.video.SDL_Window;
import java.io.Closeable;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SdlDisplay implements Closeable, Display {

  private static final Logger log = LoggerFactory.getLogger(SdlDisplay.class);

  private static final int W = 256;
  private static final int H = 240;
  private static final int SCALE = 3;
  private static final double FPS = 60.0988;
  private static final Duration FRAME_NS = Duration.ofNanos((long) (1_000_000_000.0 / FPS));

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

  private final CircularCounter logFpsLaggingThrottle;

  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final SDL_Window window;
  private final SDL_Renderer renderer;
  private final SDL_Texture texture;

  private Duration nextFrameAt = Duration.ofNanos(System.nanoTime());

  public boolean quit = false;

  public SdlDisplay(NesJoypad joypad1, NesJoypad joypad2) {
    this.logFpsLaggingThrottle = new CircularCounter(100);
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;

    if (SDL_Init(SDL_INIT_EVERYTHING) != 0) {
      throw new IllegalStateException("Unable to initialize SDL library: " + SDL_GetError());
    }

    SDL_Window.Ref windowRef = new SDL_Window.Ref();
    SDL_Renderer.Ref rendererRef = new SDL_Renderer.Ref();
    int result = SDL_CreateWindowAndRenderer(W * SCALE, H * SCALE, 0, windowRef, rendererRef);
    if (result != 0) {
      throw new IllegalStateException("Unable to create window and renderer: " + SDL_GetError());
    }
    window = windowRef.getWindow();
    renderer = rendererRef.getRenderer();

    SDL_RenderSetVSync(renderer, 1);
    SDL_RenderSetLogicalSize(renderer, W, H);
    SDL_SetRenderDrawColor(renderer, (byte) 0x00, (byte) 0xff, (byte) 0x00, (byte) 0xff);

    texture = SDL_CreateTexture(renderer, SDL_PIXELFORMAT_RGB24, SDL_TEXTUREACCESS_STATIC, W, H);
    if (texture == null) {
      throw new IllegalStateException("Unable to create SDL texture: " + SDL_GetError());
    }
  }

  @Override
  public void draw(byte[] frame) {
    Pointer pixelsPtr = new Memory(frame.length);
    pixelsPtr.write(0, frame, 0, frame.length);

    SDL_UpdateTexture(texture, null, pixelsPtr, 3 * W);
    SDL_RenderClear(renderer);
    SDL_RenderCopy(renderer, texture, null, null);
    SDL_RenderPresent(renderer);

    pump();
  }

  public void pump() {
    Duration delay = nextFrameAt.minus(System.nanoTime(), ChronoUnit.NANOS);
    nextFrameAt = nextFrameAt.plus(FRAME_NS);

    //  If FPS is lagging, just pump events and hope we catch up.
    if (!delay.isPositive()) {
      if (logFpsLaggingThrottle.tick()) {
        log.warn("FPS is lagging by {} ms", delay.abs().toMillis());
      }
      SDL_PumpEvents();
      if (SDL_HasEvent(SDL_QUIT)) {
        quit = true;
      }
      return;
    }

    SDL_Event event = new SDL_Event();
    while (SDL_WaitEventTimeout(event, (int) delay.toMillis()) == 1) {
      dispatch(event);
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
    SDL_Quit();
  }
}
