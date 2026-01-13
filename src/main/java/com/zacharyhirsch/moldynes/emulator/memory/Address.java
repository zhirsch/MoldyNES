package com.zacharyhirsch.moldynes.emulator.memory;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Address {

  interface Reader {
    byte read(int address);
  }

  interface Writer {
    void write(int address, byte data);
  }

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

  static Address of(int address, Reader reader, Writer writer) {
    return new Address() {
      @Override
      public byte read() {
        return reader.read(address);
      }

      @Override
      public void write(byte data) {
        writer.write(address, data);
      }
    };
  }

  byte read();

  void write(byte data);
}
