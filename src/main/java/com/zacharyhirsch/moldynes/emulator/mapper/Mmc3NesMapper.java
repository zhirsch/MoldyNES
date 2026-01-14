package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.nesdev.org/wiki/MMC3
// TODO: Support cartridges with hardwired 4-screen VRAM (header6.3).
final class Mmc3NesMapper extends AbstractNesMapper {

  private static final Logger log = LoggerFactory.getLogger(Mmc3NesMapper.class);

  private final NesRom rom;
  private final ByteBuffer wram;
  private final ByteBuffer vram;

  private int bankSelect;
  private final int[] bankRegisters;
  private int prgRomBankMode;
  private int chrRomBankMode;
  private NametableLayout nametableLayout;
  private int irqCounterLatch;
  private int irqCounter;
  private boolean irqCounterReload;
  private boolean irqEnable;
  private boolean irq;
  private boolean prevA12;
  private int cpuClocks = 3;

  public Mmc3NesMapper(NesRom rom) {
    this.rom = rom;
    this.wram = ByteBuffer.wrap(new byte[0x2000]);
    this.vram = ByteBuffer.wrap(new byte[0x2000]);
    this.bankSelect = 0;
    this.bankRegisters = new int[8];
    this.prgRomBankMode = 0;
    this.chrRomBankMode = 0;
    this.nametableLayout = NametableLayout.HORIZONTAL;
    this.irqCounterLatch = 0;
    this.irqCounter = 0;
    this.irqCounterReload = false;
    this.irqEnable = true;
    this.irq = false;
    this.prevA12 = false;
  }

  @Override
  public void tick(int v) {
    cpuClocks = Math.clamp(cpuClocks - 1, 0, 3);
    checkIrqCounter(v);
  }

  @Override
  public boolean irq() {
    return irq;
  }

  private void checkIrqCounter(int address) {
    boolean thisA12 = (address & 0b0001_0000_0000_0000) != 0;
    if (prevA12 && !thisA12) {
      cpuClocks = 3;
    } else if (!prevA12 && thisA12 && cpuClocks == 0) {
      tickIrqCounter();
    }
    prevA12 = thisA12;
  }

  private void tickIrqCounter() {
    if (irqCounter == 0 || irqCounterReload) {
      irqCounter = irqCounterLatch;
      irqCounterReload = false;
    } else {
      irqCounter--;
    }
    if (irqCounter == 0 && irqEnable) {
      irq = true;
    }
  }

  @Override
  protected byte readPrgRom(int address) {
    return switch (prgRomBankMode) {
      case 0 -> readPrgRomMode0(address);
      case 1 -> readPrgRomMode1(address);
      default -> throw new IllegalStateException();
    };
  }

  @Override
  protected byte readPrgRam(int address) {
    return wram.get(address - 0x6000);
  }

  @Override
  protected void writePrgRam(int address, byte data) {
    wram.put(address - 0x6000, data);
  }

  @Override
  protected byte readChrRam(int address) {
    return switch (chrRomBankMode) {
      case 0 -> readChrRomMode0(address);
      case 1 -> readChrRomMode1(address);
      default -> throw new IllegalStateException();
    };
  }

  @Override
  protected void writeChrRam(int address, byte data) {
    switch (chrRomBankMode) {
      case 0 -> writeChrRomMode0(address, data);
      case 1 -> writeChrRomMode1(address, data);
      default -> throw new IllegalStateException();
    }
  }

  @Override
  protected byte readPpuRam(int address) {
    return vram.get(mirror(address));
  }

  @Override
  protected void writePpuRam(int address, byte data) {
    vram.put(mirror(address), data);
  }

  @Override
  protected void writeRegister(int address, byte data) {
    assert 0x8000 <= address && address <= 0xffff;
    boolean even = (address & 0x0001) == 0;
    if (0x8000 <= address && address <= 0x9fff) {
      if (even) {
        bankSelect = data & 0b0000_0111;
        prgRomBankMode = (data & 0b0100_0000) >>> 6;
        chrRomBankMode = (data & 0b1000_0000) >>> 7;
      } else {
        bankRegisters[bankSelect] = data;
      }
      return;
    }
    if (0xa000 <= address && address <= 0xbfff) {
      if (even) {
        nametableLayout =
            ((data & 0b0000_0001) == 0) ? NametableLayout.VERTICAL : NametableLayout.HORIZONTAL;
      } else {
        log.warn("Ignoring PRG-RAM protect ({})", "%04x".formatted(address));
      }
      return;
    }
    if (0xc000 <= address && address <= 0xdfff) {
      if (even) {
        irqCounterLatch = data;
      } else {
        irqCounter = 0;
        irqCounterReload = true;
      }
      return;
    }
    if (0xe000 <= address && address <= 0xffff) {
      if (even) {
        irqEnable = false;
        irq = false;
      } else {
        irqEnable = true;
      }
      return;
    }
    throw new IllegalStateException();
  }

  @Override
  protected NametableLayout getNametableLayout() {
    return nametableLayout;
  }

  private byte readChrRomMode0(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x07ff) {
      return rom.chr().get((bankRegisters[0] & 0b1111_1110) * 0x0400 + (address - 0x0000));
    }
    if (0x0800 <= address && address <= 0x0fff) {
      return rom.chr().get((bankRegisters[1] & 0b1111_1110) * 0x0400 + (address - 0x0800));
    }
    if (0x1000 <= address && address <= 0x13ff) {
      return rom.chr().get(bankRegisters[2] * 0x0400 + (address - 0x1000));
    }
    if (0x1400 <= address && address <= 0x17ff) {
      return rom.chr().get(bankRegisters[3] * 0x0400 + (address - 0x1400));
    }
    if (0x1800 <= address && address <= 0x1bff) {
      return rom.chr().get(bankRegisters[4] * 0x0400 + (address - 0x1800));
    }
    if (0x1c00 <= address && address <= 0x1fff) {
      return rom.chr().get(bankRegisters[5] * 0x0400 + (address - 0x1c00));
    }
    throw new IllegalStateException();
  }

  private void writeChrRomMode0(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x07ff) {
      rom.chr().put((bankRegisters[0] & 0b1111_1110) * 0x0400 + (address - 0x0000), data);
      return;
    }
    if (0x0800 <= address && address <= 0x0fff) {
      rom.chr().put((bankRegisters[1] & 0b1111_1110) * 0x0400 + (address - 0x0800), data);
      return;
    }
    if (0x1000 <= address && address <= 0x13ff) {
      rom.chr().put(bankRegisters[2] * 0x0400 + (address - 0x1000), data);
      return;
    }
    if (0x1400 <= address && address <= 0x17ff) {
      rom.chr().put(bankRegisters[3] * 0x0400 + (address - 0x1400), data);
      return;
    }
    if (0x1800 <= address && address <= 0x1bff) {
      rom.chr().put(bankRegisters[4] * 0x0400 + (address - 0x1800), data);
      return;
    }
    if (0x1c00 <= address && address <= 0x1fff) {
      rom.chr().put(bankRegisters[5] * 0x0400 + (address - 0x1c00), data);
      return;
    }
    throw new IllegalStateException();
  }

  private byte readChrRomMode1(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x03ff) {
      return rom.chr().get(bankRegisters[2] * 0x0400 + (address - 0x0000));
    }
    if (0x0400 <= address && address <= 0x07ff) {
      return rom.chr().get(bankRegisters[3] * 0x0400 + (address - 0x0400));
    }
    if (0x0800 <= address && address <= 0x0bff) {
      return rom.chr().get(bankRegisters[4] * 0x0400 + (address - 0x0800));
    }
    if (0x0c00 <= address && address <= 0x0fff) {
      return rom.chr().get(bankRegisters[5] * 0x0400 + (address - 0x0c00));
    }
    if (0x1000 <= address && address <= 0x17ff) {
      return rom.chr().get((bankRegisters[0] & 0b1111_1110) * 0x0400 + (address - 0x1000));
    }
    if (0x1800 <= address && address <= 0x1fff) {
      return rom.chr().get((bankRegisters[1] & 0b1111_1110) * 0x0400 + (address - 0x1800));
    }
    throw new IllegalStateException();
  }

  private void writeChrRomMode1(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x03ff) {
      rom.chr().put(bankRegisters[2] * 0x0400 + (address - 0x0000), data);
      return;
    }
    if (0x0400 <= address && address <= 0x07ff) {
      rom.chr().put(bankRegisters[3] * 0x0400 + (address - 0x0400), data);
      return;
    }
    if (0x0800 <= address && address <= 0x0bff) {
      rom.chr().put(bankRegisters[4] * 0x0400 + (address - 0x0800), data);
      return;
    }
    if (0x0c00 <= address && address <= 0x0fff) {
      rom.chr().put(bankRegisters[5] * 0x0400 + (address - 0x0c00), data);
      return;
    }
    if (0x1000 <= address && address <= 0x17ff) {
      rom.chr().put((bankRegisters[0] & 0b1111_1110) * 0x0400 + (address - 0x1000), data);
      return;
    }
    if (0x1800 <= address && address <= 0x1fff) {
      rom.chr().put((bankRegisters[1] & 0b1111_1110) * 0x0400 + (address - 0x1800), data);
      return;
    }
    throw new IllegalStateException();
  }

  private byte readPrgRomMode0(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    if (0x8000 <= address && address <= 0x9fff) {
      return rom.prg().get(bankRegisters[6] * 0x2000 + address - 0x8000);
    }
    if (0xa000 <= address && address <= 0xbfff) {
      return rom.prg().get(bankRegisters[7] * 0x2000 + address - 0xa000);
    }
    if (0xc000 <= address && address <= 0xdfff) {
      int numBanks = rom.prg().capacity() / 0x2000;
      return rom.prg().get((numBanks - 2) * 0x2000 + address - 0xc000);
    }
    if (0xe000 <= address && address <= 0xffff) {
      int numBanks = rom.prg().capacity() / 0x2000;
      return rom.prg().get((numBanks - 1) * 0x2000 + address - 0xe000);
    }
    throw new IllegalStateException();
  }

  private byte readPrgRomMode1(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    if (0x8000 <= address && address <= 0x9fff) {
      int numBanks = rom.prg().capacity() / 0x2000;
      return rom.prg().get((numBanks - 2) * 0x2000 + address - 0x8000);
    }
    if (0xa000 <= address && address <= 0xbfff) {
      return rom.prg().get(bankRegisters[7] * 0x2000 + address - 0xa000);
    }
    if (0xc000 <= address && address <= 0xdfff) {
      return rom.prg().get(bankRegisters[6] * 0x2000 + address - 0xc000);
    }
    if (0xe000 <= address && address <= 0xffff) {
      int numBanks = rom.prg().capacity() / 0x2000;
      return rom.prg().get((numBanks - 1) * 0x2000 + address - 0xe000);
    }
    throw new IllegalStateException();
  }
}
