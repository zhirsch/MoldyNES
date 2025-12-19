package com.zacharyhirsch.moldynes.emulator.rom;

public record NesRom(byte[] prg, byte[] chr, NesRomProperties properties) {}
