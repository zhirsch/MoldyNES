package com.zacharyhirsch.moldynes.emulator.io;

import static com.zacharyhirsch.jna.sdl3.SDL_h.*;

import com.google.common.collect.ImmutableMap;
import java.lang.foreign.Arena;
import java.lang.foreign.MemorySegment;
import java.util.Map;

public final class SdlIo implements Io {

  private static final double FPS = 60.0988;
  private static final int CPU_CYCLES_PER_SECOND = (int) (341 * 262 * FPS / 3);

  private static final Map<Integer, Joypad.Button> JOYPAD1_KEYS =
      ImmutableMap.<Integer, Joypad.Button>builder()
          .put(SDLK_S(), Joypad.Button.DOWN)
          .put(SDLK_W(), Joypad.Button.UP)
          .put(SDLK_D(), Joypad.Button.RIGHT)
          .put(SDLK_A(), Joypad.Button.LEFT)
          .put(SDLK_Z(), Joypad.Button.SELECT)
          .put(SDLK_X(), Joypad.Button.START)
          .put(SDLK_M(), Joypad.Button.BUTTON_A)
          .put((int) ',', Joypad.Button.BUTTON_B)
          .build();

  private static final Map<Integer, Joypad.Button> JOYPAD2_KEYS =
      ImmutableMap.<Integer, Joypad.Button>builder()
          .put(SDLK_DOWN(), Joypad.Button.DOWN)
          .put(SDLK_UP(), Joypad.Button.UP)
          .put(SDLK_RIGHT(), Joypad.Button.RIGHT)
          .put(SDLK_LEFT(), Joypad.Button.LEFT)
          .put(SDLK_SPACE(), Joypad.Button.SELECT)
          .put(SDLK_RETURN(), Joypad.Button.START)
          .put(SDLK_PERIOD(), Joypad.Button.BUTTON_A)
          .put(SDLK_SLASH(), Joypad.Button.BUTTON_B)
          .build();

  private final SdlEventLoop eventLoop;
  private final SdlAudio audio;
  private final SdlVideo video;
  private final SdlJoypad joypad1;
  private final SdlJoypad joypad2;

  public SdlIo(Arena arena) {
    this.eventLoop = new SdlEventLoop(arena, this::onKeyDown, this::onKeyUp);
    this.audio = new SdlAudio(arena, CPU_CYCLES_PER_SECOND);
    this.video = new SdlVideo(arena);
    this.joypad1 = new SdlJoypad(JOYPAD1_KEYS);
    this.joypad2 = new SdlJoypad(JOYPAD2_KEYS);
  }

  @Override
  public Audio audio() {
    return audio;
  }

  @Override
  public Video video() {
    return video;
  }

  @Override
  public Joypad joypad1() {
    return joypad1;
  }

  @Override
  public Joypad joypad2() {
    return joypad2;
  }

  @Override
  public EventLoop eventLoop() {
    return eventLoop;
  }

  @Override
  public void close() {
    audio.close();
    video.close();
    SDL_Quit();
  }

  private void onKeyDown(MemorySegment keyboardEvent) {
    onKeyPress(keyboardEvent, true);
  }

  private void onKeyUp(MemorySegment keyboardEvent) {
    onKeyPress(keyboardEvent, false);
  }

  private void onKeyPress(MemorySegment keyboardEvent, boolean down) {
    joypad1.setButton(keyboardEvent, down);
    joypad2.setButton(keyboardEvent, down);
  }
}
