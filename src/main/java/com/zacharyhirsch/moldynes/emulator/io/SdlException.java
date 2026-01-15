package com.zacharyhirsch.moldynes.emulator.io;

import static com.zacharyhirsch.jna.sdl3.SDL_h.*;

final class SdlException extends RuntimeException {

  SdlException(String message) {
    super("%s: %s".formatted(message, SDL_GetError().getString(0)));
  }
}
