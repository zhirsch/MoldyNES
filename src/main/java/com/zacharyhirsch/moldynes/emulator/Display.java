package com.zacharyhirsch.moldynes.emulator;

import static io.github.libsdl4j.api.Sdl.SDL_Init;
import static io.github.libsdl4j.api.Sdl.SDL_Quit;
import static io.github.libsdl4j.api.SdlSubSystemConst.SDL_INIT_EVERYTHING;
import static io.github.libsdl4j.api.error.SdlError.SDL_GetError;
import static io.github.libsdl4j.api.event.SDL_EventType.SDL_QUIT;
import static io.github.libsdl4j.api.event.SdlEvents.SDL_PollEvent;
import static io.github.libsdl4j.api.pixels.SDL_PixelFormatEnum.SDL_PIXELFORMAT_RGB24;
import static io.github.libsdl4j.api.render.SDL_TextureAccess.SDL_TEXTUREACCESS_STREAMING;
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

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import io.github.libsdl4j.api.event.SDL_Event;
import io.github.libsdl4j.api.render.SDL_Renderer;
import io.github.libsdl4j.api.render.SDL_Texture;
import io.github.libsdl4j.api.video.SDL_Window;

final class Display implements NesPpu.DrawFrame, AutoCloseable {

  private static final int W = 256;
  private static final int H = 240;

  private final SDL_Window window;
  private final SDL_Renderer renderer;
  private final SDL_Texture texture;

  public boolean quit = false;

  public Display() {
    // Initialize SDL
    int result = SDL_Init(SDL_INIT_EVERYTHING);
    if (result != 0) {
      throw new IllegalStateException(
          "Unable to initialize SDL library (Error code " + result + "): " + SDL_GetError());
    }

    int scale = 3;

    SDL_Window.Ref windowRef = new SDL_Window.Ref();
    SDL_Renderer.Ref rendererRef = new SDL_Renderer.Ref();
    SDL_CreateWindowAndRenderer(W * scale, H * scale, 0, windowRef, rendererRef);
    window = windowRef.getWindow();
    renderer = rendererRef.getRenderer();

    SDL_RenderSetVSync(renderer, 1);
    SDL_RenderSetLogicalSize(renderer, W, H);
    SDL_SetRenderDrawColor(renderer, (byte) 0x00, (byte) 0xff, (byte) 0x00, (byte) 0xff);

    texture = SDL_CreateTexture(renderer, SDL_PIXELFORMAT_RGB24, SDL_TEXTUREACCESS_STREAMING, W, H);
    if (texture == null) {
      throw new IllegalStateException("Unable to create SDL texture: " + SDL_GetError());
    }
  }

  @Override
  public void draw(byte[] frame) {
    Pointer pixelsPtr = new Memory(frame.length);
    pixelsPtr.write(0, frame, 0, frame.length);
    SDL_UpdateTexture(texture, null, pixelsPtr, 3 * W);

    SDL_RenderCopy(renderer, texture, null, null);

    SDL_RenderPresent(renderer);

    SDL_Event evt = new SDL_Event();
    while (SDL_PollEvent(evt) != 0) {
      quit |= evt.type == SDL_QUIT;
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
