package com.zacharyhirsch.moldynes.emulator.rom;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import java.nio.ByteBuffer;

public record NesRomSection(int bankSize, ByteBuffer value) {

  public int getNumBanks() {
    return value.capacity() / bankSize();
  }

  public byte read(int bankAddr, int bankIndex) {
    return read(bankAddr, bankIndex, bankIndex + 1);
  }

  public byte read(int bankAddr, int bankIndex0, int bankIndex1) {
    if (0x0000 <= bankAddr && bankAddr < bankSize()) {
      return read(bankSize(), bankIndex0, (short) bankAddr);
    }
    if (bankSize() <= bankAddr && bankAddr < 2 * bankSize()) {
      return read(bankSize(), bankIndex1, (short) (bankAddr - bankSize()));
    }
    throw new InvalidReadError(bankAddr);
  }

  private byte read(int bankSize, int bankIndex, short bankAddr) {
    return value.get(bankIndex * bankSize + bankAddr);
  }
}
