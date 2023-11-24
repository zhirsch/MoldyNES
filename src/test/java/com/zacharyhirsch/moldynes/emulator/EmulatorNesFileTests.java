package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class EmulatorNesFileTests {

  private NesCpuMemoryMap load(String path, short entry) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }

      ByteBuffer buffer = ByteBuffer.wrap(is.readAllBytes());
      byte[] header = readHeader(buffer);

      if ((header[7] & 0x0c) == 0x08) {
        throw new RuntimeException("NES 2.0 file format not implemented");
      }
      return parseNes1Format(header, buffer, entry);
    }
  }

  private static byte[] readHeader(ByteBuffer buffer) {
    byte[] header = new byte[16];
    buffer.get(0, header);
    if (!checkMagic(header)) {
      throw new IllegalArgumentException("bad magic string");
    }
    return header;
  }

  private static boolean checkMagic(byte[] header) {
    return new String(header, 0, 4).equals("NES\u001a");
  }

  private NesCpuMemoryMap parseNes1Format(byte[] header, ByteBuffer buffer, short entry) {
    byte mapper = (byte) ((header[7] & 0b1111_0000) | ((header[6] & 0b1111_0000) >>> 4));
    setEntryPoint(buffer, entry);
    return NesCpuMemoryMapFactory.get(mapper).load(header, buffer);
  }

  private void setEntryPoint(ByteBuffer buffer, short entry) {
    buffer.put(0xfffc - 0xc000 + 16, (byte) (entry >>> 0));
    buffer.put(0xfffd - 0xc000 + 16, (byte) (entry >>> 8));
  }

  // https://www.qmtpro.com/~nes/misc/nestest.txt
  @Test
  void nestest() throws IOException {
    NesCpuMemoryMap memory = load("nestest.nes", (short) 0xc000);
    new Emulator(memory, System.out).run();

    assertThat(memory.fetch((byte) 0x00, (byte) 0x02)).isEqualTo(0);
    assertThat(memory.fetch((byte) 0x00, (byte) 0x03)).isEqualTo(0);
  }
}
