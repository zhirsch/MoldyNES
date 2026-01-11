package com.zacharyhirsch.moldynes.emulator.memory;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Address {

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

  byte read();

  void write(byte data);
}
