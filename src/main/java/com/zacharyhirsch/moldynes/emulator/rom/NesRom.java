package com.zacharyhirsch.moldynes.emulator.rom;

public record NesRom(NesRomSection prg, NesRomSection chr, NesRomProperties properties) {}
