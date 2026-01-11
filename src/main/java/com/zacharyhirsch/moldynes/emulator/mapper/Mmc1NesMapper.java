package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidReadError;
import com.zacharyhirsch.moldynes.emulator.memory.InvalidWriteError;
import com.zacharyhirsch.moldynes.emulator.rom.NesRom;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.Optional;

// https://www.nesdev.org/wiki/MMC1
final class Mmc1NesMapper implements NesMapper {

  private final NesRom rom;
  private final ByteBuffer prgRam;
  private final ShiftRegister shiftRegister;

  private int nametableArrangement;
  private int prgRomBankMode;
  private int prgRomBankSelect;
  private int chrRomBankMode;
  private int chrRomBank0Select;
  private int chrRomBank1Select;
  private boolean prgRamEnabled;

  public Mmc1NesMapper(NesRom rom) {
    this.rom = rom;
    this.prgRam = ByteBuffer.wrap(new byte[0x2000]);
    this.shiftRegister = new ShiftRegister();
    this.nametableArrangement = 0;
    this.prgRomBankMode = 0;
    this.prgRomBankSelect = 0;
    this.chrRomBankMode = 0;
    this.chrRomBank0Select = 0;
    this.chrRomBank1Select = 0;
    this.prgRamEnabled = true;
  }

  @Override
  public Address resolveCpu(int address) {
    assert 0x0000 <= address && address <= 0xffff;
    if (0x0000 <= address && address <= 0x5fff) {
      return Address.of(address, InvalidReadError::throw_, InvalidWriteError::throw_);
    }
    if (0x6000 <= address && address <= 0x7fff) {
      if (!prgRamEnabled) {
        return Address.of(address, InvalidReadError::throw_, InvalidWriteError::throw_);
      }
      return Address.of(address - 0x6000, prgRam::get, prgRam::put);
    }
    if (0x8000 <= address && address <= 0x9fff) {
      return Address.of(() -> readPrgRom(address), this::writeControlRegister);
    }
    if (0xa000 <= address && address <= 0xbfff) {
      return Address.of(() -> readPrgRom(address), this::writeChrBank0Register);
    }
    if (0xc000 <= address && address <= 0xdfff) {
      return Address.of(() -> readPrgRom(address), this::writeChrBank1Register);
    }
    if (0xe000 <= address && address <= 0xffff) {
      return Address.of(() -> readPrgRom(address), this::writePrgBankRegister);
    }
    throw new IllegalStateException();
  }

  @Override
  public Address resolvePpu(int address, ByteBuffer ppuRam) {
    assert 0x0000 <= address && address <= 0x3fff;
    if (0x0000 <= address && address <= 0x1fff) {
      return Address.of(address, this::readChrRom, rom.chr()::put);
    }
    if (0x2000 <= address && address <= 0x3eff) {
      return Address.of(mirror(address), ppuRam::get, ppuRam::put);
    }
    if (0x3f00 <= address && address <= 0x3fff) {
      return Address.of(mirror(address), ppuRam::get, InvalidWriteError::throw_);
    }
    throw new IllegalStateException();
  }

  private byte readChrRom(int address) {
    assert 0x0000 <= address && address <= 0x3fff;
    int bank0 =
        switch (chrRomBankMode) {
          case 0 -> chrRomBank0Select & 0b1111_1110;
          case 1 -> chrRomBank0Select;
          default -> throw new IllegalStateException();
        };
    int bank1 =
        switch (chrRomBankMode) {
          case 0 -> bank0 + 1;
          case 1 -> chrRomBank1Select;
          default -> throw new IllegalStateException();
        };
    if (0x0000 <= address && address <= 0x1fff) {
      return rom.chr().get(bank0 * 0x2000 + address - 0x0000);
    }
    if (0x2000 <= address && address <= 0x3fff) {
      return rom.chr().get(bank1 * 0x2000 + address - 0x2000);
    }
    throw new InvalidReadError(address);
  }

  private byte readPrgRom(int address) {
    assert 0x8000 <= address && address <= 0xffff;
    int bank0 =
        switch (prgRomBankMode) {
          case 0 -> prgRomBankSelect & 0b1111_1110;
          case 1 -> prgRomBankSelect & 0b1111_1110;
          case 2 -> 0;
          case 3 -> prgRomBankSelect;
          default -> throw new IllegalStateException();
        };
    int bank1 =
        switch (prgRomBankMode) {
          case 0 -> bank0 + 1;
          case 1 -> bank0 + 1;
          case 2 -> prgRomBankSelect;
          case 3 -> rom.prg().capacity() / 0x4000 - 1;
          default -> throw new IllegalStateException();
        };
    if (0x8000 <= address && address <= 0xbfff) {
      return rom.prg().get(bank0 * 0x4000 + address - 0x8000);
    }
    if (0xc000 <= address && address <= 0xffff) {
      return rom.prg().get(bank1 * 0x4000 + address - 0xc000);
    }
    throw new InvalidReadError(address);
  }

  private void writeControlRegister(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
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
    value.ifPresent(val -> nametableArrangement = val & 0b0000_0011);
    value.ifPresent(val -> prgRomBankMode = (val & 0b0000_1100) >>> 2);
    value.ifPresent(val -> chrRomBankMode = (val & 0b0001_0000) >>> 4);
  }

  private void writeChrBank0Register(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> chrRomBank0Select = val);
  }

  private void writeChrBank1Register(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> chrRomBank1Select = val);
  }

  private void writePrgBankRegister(byte data) {
    if ((data & 0b1000_0000) == 0b1000_0000) {
      reset();
      return;
    }
    Optional<Byte> value = shiftRegister.shift(data);
    value.ifPresent(val -> prgRomBankSelect = val & 0b0000_1111);
    value.ifPresent(val -> prgRamEnabled = (val & 0b0001_0000) == 0b0000_0000);
  }

  private void reset() {
    shiftRegister.reset();
    prgRomBankMode = 3;
  }

  private short mirror(int addr) {
    // Horizontal mirroring:
    //   [ A ] [ a ]
    //   [ B ] [ b ]
    //
    // Vertical mirroring:
    //   [ A ] [ B ]
    //   [ a ] [ b ]

    return switch (nametableArrangement) {
      case 0 -> (short) (addr & 0b0000_0011_1111_1111);
      case 1 -> throw new UnsupportedOperationException(String.valueOf(nametableArrangement));
      case 2 -> (short) ((0b0000_0000_0000_0000) | (addr & 0b0000_0011_1111_1111));
      case 3 -> (short) ((0b0000_0100_0000_0000) | (addr & 0b0000_0011_1111_1111));
      default -> throw new IllegalStateException(String.valueOf(nametableArrangement));
    };
  }

  private static final class ShiftRegister {

    private final BitSet value;

    private int count;

    ShiftRegister() {
      this.value = new BitSet(5);
      this.count = 0;
    }

    Optional<Byte> shift(byte bit) {
      value.set(count++, (bit & 0b0000_0001) == 0b0000_0001);
      if (count < 5) {
        return Optional.empty();
      }
      byte v = value.isEmpty() ? 0 : value.toByteArray()[0];
      reset();
      return Optional.of(v);
    }

    public void reset() {
      value.clear();
      count = 0;
    }
  }
}
