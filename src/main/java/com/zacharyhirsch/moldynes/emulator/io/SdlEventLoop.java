package com.zacharyhirsch.moldynes.emulator.io;

import static com.zacharyhirsch.jna.sdl3.SDL_h.SDL_EVENT_KEY_DOWN;
import static com.zacharyhirsch.jna.sdl3.SDL_h.SDL_EVENT_KEY_UP;
import static com.zacharyhirsch.jna.sdl3.SDL_h.SDL_EVENT_QUIT;
import static com.zacharyhirsch.jna.sdl3.SDL_h.SDL_PollEvent;

import com.zacharyhirsch.jna.sdl3.SDL_Event;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.function.Consumer;

final class SdlEventLoop implements EventLoop {

  private final MemorySegment event;
  private final Consumer<MemorySegment> onKeyDown;
  private final Consumer<MemorySegment> onKeyUp;

  private boolean quit = false;

  SdlEventLoop(Arena arena, Consumer<MemorySegment> onKeyDown, Consumer<MemorySegment> onKeyUp) {
    this.event = SDL_Event.allocate(arena);
    this.onKeyDown = onKeyDown;
    this.onKeyUp = onKeyUp;
  }

  @Override
  public void run(Runnable tick) {
    int i = 0;
    while (!quit) {
      tick.run();
      if ((i++ % 100) == 0) {
        pump();
      }
    }
  }

  public void pump() {
    while (SDL_PollEvent(event)) {
      int type = SDL_Event.type(event);
      if (type == SDL_EVENT_QUIT()) {
        quit = true;
        continue;
      }
      if (type == SDL_EVENT_KEY_DOWN()) {
        onKeyDown.accept(SDL_Event.key(event));
        continue;
      }
      if (type == SDL_EVENT_KEY_UP()) {
        onKeyUp.accept(SDL_Event.key(event));
        continue;
      }
    }
  }
}
