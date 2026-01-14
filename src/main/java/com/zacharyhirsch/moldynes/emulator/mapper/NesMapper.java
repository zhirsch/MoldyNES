package com.zacharyhirsch.moldynes.emulator.mapper;

import com.zacharyhirsch.moldynes.emulator.memory.Address;

public interface NesMapper {

  void tick(int v);

  boolean irq();

  Address resolveCpu(int address);

  Address resolvePpu(int address);
}
