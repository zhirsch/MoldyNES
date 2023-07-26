package com.zacharyhirsch.moldynes.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Test;

public class EmulatorNesFileTests {

  private NesCpuMemory load(String path) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }

      ByteBuffer buffer = ByteBuffer.wrap(is.readAllBytes());
      byte[] header = readHeader(buffer);

      if ((header[7] & 0x0c) == 0x08) {
        throw new RuntimeException("NES 2.0 file format not implemented");
      }
      return parseNes1Format(header, buffer);
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

  private NesCpuMemory parseNes1Format(byte[] header, ByteBuffer buffer) {
    byte mapper = (byte) ((header[7] & 0b1111_0000) | ((header[6] & 0b1111_0000) >>> 4));
    return NesCpuMemoryMap.get(mapper).load(header, buffer);
  }

  // https://www.qmtpro.com/~nes/misc/nestest.txt
  @Test
  void functionalTest() throws IOException {
    NesCpuMemory memory = load("nestest.nes");
    Emulator emulator = new Emulator(memory, (short) 0xc000);
    emulator.run();
  }
}