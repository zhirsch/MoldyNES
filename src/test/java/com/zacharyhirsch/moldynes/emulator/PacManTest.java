package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.cpu.logging.NesCpuLogger;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemory;
import com.zacharyhirsch.moldynes.emulator.memory.NesMemoryMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class PacManTest {

  private ByteBuffer read(String path) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }
      return ByteBuffer.wrap(is.readAllBytes());
    }
  }

  @Test
  void pacman() throws IOException {
    try (Display display = new Display()) {
      ByteBuffer buffer = read("pacman.nes");
      NesMemoryMapper mapper = NesMemoryMapper.get(buffer);

      NesPpu ppu = mapper.createPpu(buffer, display);
      NesApu apu = new NesApu();
      NesMemory memory = mapper.createMem(buffer, ppu, apu);

      NesCpu cpu = new NesCpu(ppu, memory, new NesCpuLogger(OutputStream.nullOutputStream()));
      Emulator emulator = new Emulator(cpu, ppu, apu);
      while (emulator.step()) {
        if (display.quit) {
          break;
        }
      }

      assertThat(memory.fetch((byte) 0x00, (byte) 0x02)).isEqualTo(0);
      assertThat(memory.fetch((byte) 0x00, (byte) 0x03)).isEqualTo(0);
    }
  }
}
