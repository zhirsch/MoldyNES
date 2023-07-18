package com.zacharyhirsch.moldynes.emulator;

import java.io.IOException;
import java.io.InputStream;
import org.junit.jupiter.api.Test;

class EmulatorFunctionalTest {

  private byte[] readImage(String path) throws IOException {
    try (InputStream is = getClass().getClassLoader().getResourceAsStream(path)) {
      if (is == null) {
        throw new RuntimeException("image " + path + " does not exist");
      }
      return is.readAllBytes();
    }
  }

  @Test
  void functionalTest() throws IOException {
    byte[] ram = readImage("6502_65C02_functional_tests/bin_files/6502_functional_test.bin");
    Emulator emulator = new Emulator(ram, (short) 0x0400);
    emulator.run();
  }
}
