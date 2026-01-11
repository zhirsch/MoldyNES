package com.zacharyhirsch.moldynes.emulator.rom;

import java.nio.ByteBuffer;

public record NesRom(ByteBuffer prg, ByteBuffer chr, NesRomProperties properties) {}
