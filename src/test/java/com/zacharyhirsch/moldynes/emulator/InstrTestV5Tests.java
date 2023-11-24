package com.zacharyhirsch.moldynes.emulator;

import static com.google.common.truth.Truth.assertThat;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.time.Instant;
import org.junit.jupiter.api.Test;

public class InstrTestV5Tests {

  private NesCpuMemoryMap load(String filename, Short entry) throws IOException {
    String path = "instr_test-v5\\rom_singles\\" + filename;
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

  private NesCpuMemoryMap parseNes1Format(byte[] header, ByteBuffer buffer, Short entry) {
    byte mapper = (byte) ((header[7] & 0b1111_0000) | ((header[6] & 0b1111_0000) >>> 4));
    if (entry != null) {
      setEntryPoint(buffer, entry);
    }
    return NesCpuMemoryMapFactory.get(mapper).load(header, buffer);
  }

  private void setEntryPoint(ByteBuffer buffer, short entry) {
    buffer.put(0xfffc - 0xc000 + 16, (byte) (entry >>> 0));
    buffer.put(0xfffd - 0xc000 + 16, (byte) (entry >>> 8));
  }

  private void runTest(String filename) throws IOException {
//    try (OutputStream output = new FileOutputStream(filename + ".log")) {
    try (OutputStream output = new ByteArrayOutputStream()) {
      NesCpuMemoryMap memory = load(filename, null);
      Emulator emulator = new Emulator(memory, output);
      byte status = run(emulator, memory);

      assertThat(status).isEqualTo(0x00);
    }
  }

  @Test
  void basics() throws Exception {
    runTest("01-basics.nes");
  }

  @Test
  void implied() throws Exception {
    runTest("02-implied.nes");
  }

  @Test
  void immediate() throws Exception {
    runTest("03-immediate.nes");
  }

  @Test
  void zero_page() throws Exception {
    runTest("04-zero_page.nes");
  }

  @Test
  void zp_xy() throws Exception {
    runTest("05-zp_xy.nes");
  }

  @Test
  void absolute() throws Exception {
    runTest("06-absolute.nes");
  }

  @Test
  void abs_xy() throws Exception {
    runTest("07-abs_xy.nes");
  }

  @Test
  void ind_x() throws Exception {
    runTest("08-ind_x.nes");
  }

  @Test
  void ind_y() throws Exception {
    runTest("09-ind_y.nes");
  }

  @Test
  void branches() throws Exception {
    runTest("10-branches.nes");
  }

  @Test
  void stack() throws Exception {
    runTest("11-stack.nes");
  }

  @Test
  void jmp_jsr() throws Exception {
    runTest("12-jmp_jsr.nes");
  }

  @Test
  void rts() throws Exception {
    runTest("13-rts.nes");
  }

  @Test
  void rti() throws Exception {
    runTest("14-rti.nes");
  }

  @Test
  void brk() throws Exception {
    runTest("15-brk.nes");
  }

  @Test
  void special() throws Exception {
    runTest("16-special.nes");
  }

  private static byte run(Emulator emulator, NesCpuMemoryMap memory) {
    waitForStart(emulator, memory);
    Byte lastStatus = null;
    String lastMessage = null;
    while (emulator.step()) {
      byte status = memory.fetch((byte) 0x60, (byte) 0x00);
      if (lastStatus == null || lastStatus != status) {
        lastStatus = status;
        System.out.printf("STATUS: %02x\n", status);
      }
      if (Byte.toUnsignedInt(status) < 0x80) {
        return status;
      }
      if (status == (byte) 0x81) {
        reset(emulator, memory);
      }
      StringBuilder builder = new StringBuilder();
      for (short addr = 0x6004; ; addr += 1) {
        byte ch = memory.fetch((byte) (addr >>> 8), (byte) (addr & 0xff));
        if (ch == 0) {
          break;
        }
        builder.append((char) ch);
      }
      String message = builder.toString();
      if (lastMessage == null || !lastMessage.equals(message)) {
        lastMessage = message;
        System.out.printf(">%s<\n", message);
      }
    }
    return (byte) 0xff;
  }

  private static void reset(Emulator emulator, NesCpuMemoryMap memory) {
    Instant resetAt = Instant.now().plusMillis(250);
    while (emulator.step()) {
      if (Instant.now().isAfter(resetAt)) {
        emulator.reset();
        break;
      }
    }
    waitForStart(emulator, memory);
  }

  private static void waitForStart(Emulator emulator, NesCpuMemoryMap memory) {
    while (emulator.step()) {
      byte status = memory.fetch((byte) 0x60, (byte) 0x00);
      if (status == (byte) 0x80) {
        break;
      }
    }
  }
}
