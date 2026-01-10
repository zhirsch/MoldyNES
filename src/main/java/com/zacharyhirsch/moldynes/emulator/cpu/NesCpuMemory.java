package com.zacharyhirsch.moldynes.emulator.cpu;

import com.zacharyhirsch.moldynes.emulator.apu.NesApu;
import com.zacharyhirsch.moldynes.emulator.io.NesJoypad;
import com.zacharyhirsch.moldynes.emulator.mapper.NesMapper;
import com.zacharyhirsch.moldynes.emulator.ppu.NesPpu;
import java.nio.ByteBuffer;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

final class NesCpuMemory {

  private final NesMapper mapper;
  private final NesPpu ppu;
  private final NesApu apu;
  private final NesJoypad joypad1;
  private final NesJoypad joypad2;
  private final Consumer<Byte> startOamDma;
  private final ByteBuffer ram;

  NesCpuMemory(
      NesMapper mapper,
      NesPpu ppu,
      NesApu apu,
      NesJoypad joypad1,
      NesJoypad joypad2,
      Consumer<Byte> startOamDma) {
    this.mapper = mapper;
    this.ppu = ppu;
    this.apu = apu;
    this.joypad1 = joypad1;
    this.joypad2 = joypad2;
    this.startOamDma = startOamDma;
    this.ram = ByteBuffer.wrap(new byte[0x0800]);
  }

  byte read(short address) {
    return resolve(Short.toUnsignedInt(address)).read();
  }

  void write(short address, byte data) {
    resolve(Short.toUnsignedInt(address)).write(data);
  }

  @SuppressWarnings("DuplicateBranchesInSwitch")
  private Address resolve(int address) {
    assert 0x0000 <= address && address <= 0xffff;
    if (0x0000 <= address && address <= 0x1fff) {
      return Address.of(address & 0b0000_0111_1111_1111, ram::get, ram::put);
    }
    if (0x2000 <= address && address <= 0x3fff) {
      return switch (address & 0b0010_0000_0000_0111) {
        case 0x2000 -> Address.of(() -> (byte) 0, ppu::writeControl);
        case 0x2001 -> Address.of(ppu::readMask, ppu::writeMask);
        case 0x2002 -> Address.of(ppu::readStatus, _ -> {});
        case 0x2003 -> Address.of(() -> (byte) 0, ppu::writeOamAddr);
        case 0x2004 -> Address.of(ppu::readOamData, ppu::writeOamData);
        case 0x2005 -> Address.of(() -> (byte) 0, ppu::writeScroll);
        case 0x2006 -> Address.of(() -> (byte) 0, ppu::writeAddress);
        case 0x2007 -> Address.of(ppu::readData, ppu::writeData);
        default -> throw new IllegalStateException();
      };
    }
    if (0x4000 <= address && address <= 0x4017) {
      return switch (address) {
        case 0x4000 -> Address.of(() -> (byte) 0, apu.pulse1()::writeControl);
        case 0x4001 -> Address.of(() -> (byte) 0, apu.pulse1()::writeSweep);
        case 0x4002 -> Address.of(() -> (byte) 0, apu.pulse1()::writeTimerLo);
        case 0x4003 -> Address.of(() -> (byte) 0, apu.pulse1()::writeTimerHi);
        case 0x4004 -> Address.of(() -> (byte) 0, apu.pulse2()::writeControl);
        case 0x4005 -> Address.of(() -> (byte) 0, apu.pulse2()::writeSweep);
        case 0x4006 -> Address.of(() -> (byte) 0, apu.pulse2()::writeTimerLo);
        case 0x4007 -> Address.of(() -> (byte) 0, apu.pulse2()::writeTimerHi);
        case 0x4008 -> Address.of(() -> (byte) 0, apu.triangle()::writeControl);
        case 0x4009 -> Address.of(() -> (byte) 0, _ -> {});
        case 0x400a -> Address.of(() -> (byte) 0, apu.triangle()::writeTimerLo);
        case 0x400b -> Address.of(() -> (byte) 0, apu.triangle()::writeTimerHi);
        case 0x400c -> Address.of(() -> (byte) 0, apu.noise()::writeControl);
        case 0x400d -> Address.of(() -> (byte) 0, _ -> {});
        case 0x400e -> Address.of(() -> (byte) 0, apu.noise()::writeMode);
        case 0x400f -> Address.of(() -> (byte) 0, apu.noise()::writeLength);
        case 0x4010 -> Address.of(() -> (byte) 0, apu.dmc()::writeControl);
        case 0x4011 -> Address.of(() -> (byte) 0, apu.dmc()::writeDac);
        case 0x4012 -> Address.of(() -> (byte) 0, apu.dmc()::writeAddress);
        case 0x4013 -> Address.of(() -> (byte) 0, apu.dmc()::writeLength);
        case 0x4014 -> Address.of(() -> (byte) 0, startOamDma);
        case 0x4015 -> Address.of(apu::readStatus, apu::writeStatus);
        case 0x4016 ->
            Address.of(
                joypad1::read,
                data -> {
                  joypad1.write(data);
                  joypad2.write(data);
                });
        case 0x4017 -> Address.of(joypad2::read, apu::writeFrameCounter);
        default -> throw new IllegalStateException();
      };
    }
    if (0x4018 <= address && address <= 0x401f) {
      return Address.of(() -> (byte) 0, unused -> {});
    }
    if (0x4020 <= address && address <= 0xffff) {
      return Address.of((short) address, mapper::readCpu, mapper::writeCpu);
    }
    throw new IllegalStateException();
  }

  private interface Address {

    static Address of(Supplier<Byte> read, Consumer<Byte> write) {
      return new Address() {
        @Override
        public byte read() {
          return read.get();
        }

        @Override
        public void write(byte data) {
          write.accept(data);
        }
      };
    }

    static Address of(int address, Function<Integer, Byte> read, BiConsumer<Integer, Byte> write) {
      return new Address() {
        @Override
        public byte read() {
          return read.apply(address);
        }

        @Override
        public void write(byte data) {
          write.accept(address, data);
        }
      };
    }

    static Address of(short address, Function<Short, Byte> read, BiConsumer<Short, Byte> write) {
      return new Address() {
        @Override
        public byte read() {
          return read.apply(address);
        }

        @Override
        public void write(byte data) {
          write.accept(address, data);
        }
      };
    }

    byte read();

    void write(byte data);
  }
}
