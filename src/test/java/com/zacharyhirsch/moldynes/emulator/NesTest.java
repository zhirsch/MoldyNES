package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.cpu.NesCpu;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class NesTest {

  private ByteBuffer read(String path) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }
      return ByteBuffer.wrap(is.readAllBytes());
    }
  }

  // https://www.qmtpro.com/~nes/misc/nestest.txt
  @Test
  void nestest() throws IOException {
    ByteBuffer buffer = read("nestest.nes");
    NesMapper mapper = NesMapper.get(buffer);
    NesApu apu = new NesApu();
    NesJoypad joypad1 = new NesJoypad();
    NesJoypad joypad2 = new NesJoypad();

    try (Display display = new Display(joypad1, joypad2)) {
      NesPpu ppu = new NesPpu(mapper, display);
      NesBus bus = new NesBus(mapper, ppu, joypad1, joypad2);
      NesCpu cpu = new NesCpu(ppu, bus);

      Emulator emulator = new Emulator(cpu, ppu, apu);
      while (emulator.step()) {
        if (display.quit) {
          break;
        }
      }

      assertThat(bus.read((byte) 0x00, (byte) 0x02)).isEqualTo(0);
      assertThat(bus.read((byte) 0x00, (byte) 0x03)).isEqualTo(0);
    }
  }
}
