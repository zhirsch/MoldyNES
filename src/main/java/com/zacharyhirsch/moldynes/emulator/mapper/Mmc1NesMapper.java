package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NametableLayout;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// https://www.nesdev.org/wiki/MMC1
final class Mmc1NesMapper extends AbstractNesMapper {

  private static final Logger log = LoggerFactory.getLogger(Mmc1NesMapper.class);

  private final NesRom rom;
  private final ByteBuffer wram;
  private final ByteBuffer vram;
  private final ShiftRegister shiftRegister;

  // 4bit0
  // -----
  // CPPMM
  // |||||
  // |||++- Nametable arrangement: (0: one-screen, lower bank; 1: one-screen, upper bank;
  // |||               2: horizontal arrangement ("vertical mirroring", PPU A10);
  // |||               3: vertical arrangement ("horizontal mirroring", PPU A11) )
  // |++--- PRG-ROM bank mode (0, 1: switch 32 KB at $8000, ignoring low bit of bank number;
  // |                         2: fix first bank at $8000 and switch 16 KB bank at $C000;
  // |                         3: fix last bank at $C000 and switch 16 KB bank at $8000)
  // +----- CHR-ROM bank mode (0: switch 8 KB at a time; 1: switch two separate 4 KB banks)
  private int control;

  private int prgRomBankSelect;
  private int chrRomBank0Select;
  private int chrRomBank1Select;
  private boolean prgRamEnabled;

  public Mmc1NesMapper(NesRom rom, ByteBuffer wram) {
    this.rom = rom;
    this.wram = wram;
    this.vram = ByteBuffer.wrap(new byte[0x2000]);
    this.shiftRegister = new ShiftRegister();
    this.control = 0;
    this.prgRomBankSelect = 0;
    this.chrRomBank0Select = 0;
    this.chrRomBank1Select = 0;
    this.prgRamEnabled = true;
  }

  @Override
  protected byte readPrgRam(int address) {
    if (!prgRamEnabled) {
      throw new InvalidReadError(address);
    }
    return wram.get(address - 0x6000);
  }

  @Override
  protected void writePrgRam(int address, byte data) {
    if (!prgRamEnabled) {
      throw new InvalidWriteError(address);
    }
    wram.put(address - 0x6000, data);
  }

  @Override
  protected byte readPrgRom(int address) {
    return switch (control & 0b0000_1100) {
      case 0b0000_0000, 0b0000_0100 -> readPrgRomMode01(address - 0x8000);
      case 0b0000_1000 -> readPrgRomMode2(address - 0x8000);
      case 0b0000_1100 -> readPrgRomMode3(address - 0x8000);
      default -> throw new IllegalStateException();
    };
  }

  @Override
  protected byte readChrRam(int address) {
    return switch (control & 0b0001_0000) {
      case 0b0000_0000 -> readChrRomMode0(address);
      case 0b0001_0000 -> readChrRomMode1(address);
      default -> throw new IllegalStateException();
    };
  }

  @Override
  protected void writeChrRam(int address, byte data) {
    switch (control & 0b0001_0000) {
      case 0b0000_0000 -> writeChrRomMode0(address, data);
      case 0b0001_0000 -> writeChrRomMode1(address, data);
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
    if ((data & 0b1000_0000) != 0) {
      shiftRegister.reset();
      writeControlRegister((byte) (control | 0x0c));
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    if (value.isEmpty()) {
      return;
    }
    if (0x8000 <= address && address <= 0x9fff) {
      writeControlRegister(value.get());
      return;
    }
    if (0xa000 <= address && address <= 0xbfff) {
      writeChrBank0Register(value.get());
      return;
    }
    if (0xc000 <= address && address <= 0xdfff) {
      writeChrBank1Register(value.get());
      return;
    }
    if (0xe000 <= address && address <= 0xffff) {
      writePrgBankRegister(value.get());
      return;
    }
    throw new IllegalStateException();
  }

  @Override
  protected int mirror(int address) {
    if ((control & 0b0000_0011) == 0) {
      return (short) (address & 0b0000_0011_1111_1111);
    }
    if ((control & 0b0000_0011) == 1) {
      throw new UnsupportedOperationException(String.valueOf(control));
    }
    return super.mirror(address);
  }

  @Override
  protected NametableLayout getNametableLayout() {
    return (control & 0b0000_0011) == 2 ? NametableLayout.VERTICAL : NametableLayout.HORIZONTAL;
  }

  private byte readChrRomMode0(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    return rom.chr().get((chrRomBank0Select & 0b0001_1110) * 0x1000 + address);
  }

  private byte readChrRomMode1(int address) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x0fff) {
      return rom.chr().get((chrRomBank0Select & 0b0001_1111) * 0x1000 + address);
    }
    if (0x1000 <= address && address <= 0x1fff) {
      return rom.chr().get((chrRomBank1Select & 0b0001_1111) * 0x1000 + address - 0x1000);
    }
    throw new IllegalStateException();
  }

  private void writeChrRomMode0(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    rom.chr().put((chrRomBank0Select & 0b0001_1110) * 0x1000 + address, data);
  }

  private void writeChrRomMode1(int address, byte data) {
    assert 0x0000 <= address && address <= 0x1fff;
    if (0x0000 <= address && address <= 0x0fff) {
      rom.chr().put((chrRomBank0Select & 0b0001_1111) * 0x1000 + address, data);
      return;
    }
    if (0x1000 <= address && address <= 0x1fff) {
      rom.chr().put((chrRomBank1Select & 0b0001_1111) * 0x1000 + address - 0x1000, data);
      return;
    }
    throw new IllegalStateException();
  }

  private byte readPrgRomMode01(int offset) {
    assert 0x0000 <= offset && offset <= 0x7fff;
    return rom.prg().get((prgRomBankSelect & 0b0000_1110) * 0x8000 + offset);
  }

  private byte readPrgRomMode2(int offset) {
    assert 0x0000 <= offset && offset <= 0x7fff;
    if (0x0000 <= offset && offset <= 0x3fff) {
      return rom.prg().get(offset);
    }
    if (0x4000 <= offset && offset <= 0x7fff) {
      return rom.prg().get((prgRomBankSelect & 0b0000_1111) * 0x4000 + offset - 0x4000);
    }
    throw new IllegalStateException();
  }

  private byte readPrgRomMode3(int offset) {
    assert 0x0000 <= offset && offset <= 0x7fff;
    if (0x0000 <= offset && offset <= 0x3fff) {
      return rom.prg().get((prgRomBankSelect & 0b0000_1111) * 0x4000 + offset);
    }
    if (0x4000 <= offset && offset <= 0x7fff) {
      return rom.prg().get(rom.prg().capacity() - 0x4000 + offset - 0x4000);
    }
    throw new IllegalStateException();
  }

  private void writeControlRegister(byte data) {
    int oldControl = control;
    control = data;
    if ((oldControl & 0b0000_0011) != (control & 0b0000_0011)) {
      log.info(
          "Changing nametable arrangement from {} to {}",
          (oldControl & 0b0000_0011),
          (control & 0b0000_0011));
    }
    if ((oldControl & 0b0000_1100) != (control & 0b0000_1100)) {
      log.info(
          "Changing PRG-ROM bank mode from {} to {}",
          (oldControl & 0b0000_1100) >>> 2,
          (control & 0b0000_1100) >>> 2);
    }
    if ((oldControl & 0b0001_0000) != (control & 0b0001_0000)) {
      log.info(
          "Changing CHR-ROM bank mode from {} to {}",
          (oldControl & 0b0001_0000) >>> 4,
          (control & 0b0001_0000) >>> 4);
    }
  }

  private void writeChrBank0Register(byte data) {
    chrRomBank0Select = data;
  }

  private void writeChrBank1Register(byte data) {
    chrRomBank1Select = data;
  }

  private void writePrgBankRegister(byte data) {
    prgRomBankSelect = data & 0b0000_1111;
    prgRamEnabled = (data & 0b0001_0000) == 0b0000_0000;
  }

  private static final class ShiftRegister {

    private int value;
    private int count;

    ShiftRegister() {
      this.value = 0;
      this.count = 0;
    }

    Optional<Byte> shift(byte bit) {
      value = (value | ((bit & 0b0000_0001) << count));
      count++;
      if (count < 5) {
        return Optional.empty();
      }
      int v = value;
      reset();
      return Optional.of((byte) v);
    }

    public void reset() {
      value = 0;
      count = 0;
    }
  }
}
