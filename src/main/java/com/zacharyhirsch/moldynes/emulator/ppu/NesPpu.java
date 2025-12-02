package com.zacharyhirsch.moldynes.emulator.ppu;

import com.zacharyhirsch.moldynes.emulator.Display;
import com.zacharyhirsch.moldynes.emulator.mappers.NesMapper;
import java.util.Arrays;
import java.util.function.Consumer;

public final class NesPpu {

  private interface TickFn extends Consumer<NesPpu> {}

  private static final TickFn[][] VISIBLE_SCANLINE = {
    /*   0 */ {},
    /*   1 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*   2 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*   3 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*   4 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*   5 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*   6 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*   7 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*   8 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*   9 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  10 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  11 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  12 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  13 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  14 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  15 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  16 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  17 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  18 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  19 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  20 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  21 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  22 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  23 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  24 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  25 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  26 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  27 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  28 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  29 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  30 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  31 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  32 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  33 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  34 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  35 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  36 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  37 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  38 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  39 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  40 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  41 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  42 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  43 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  44 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  45 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  46 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  47 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  48 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  49 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  50 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  51 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  52 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  53 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  54 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  55 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  56 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  57 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  58 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  59 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  60 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  61 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  62 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  63 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  64 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  65 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  66 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  67 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  68 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  69 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  70 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  71 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  72 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  73 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  74 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  75 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  76 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  77 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  78 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  79 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  80 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  81 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  82 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  83 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  84 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  85 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  86 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  87 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  88 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  89 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  90 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  91 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /*  92 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /*  93 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /*  94 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /*  95 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /*  96 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /*  97 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /*  98 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /*  99 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 100 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 101 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 102 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 103 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 104 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 105 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 106 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 107 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 108 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 109 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 110 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 111 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 112 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 113 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 114 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 115 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 116 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 117 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 118 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 119 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 120 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 121 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 122 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 123 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 124 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 125 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 126 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 127 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 128 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 129 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 130 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 131 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 132 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 133 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 134 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 135 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 136 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 137 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 138 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 139 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 140 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 141 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 142 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 143 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 144 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 145 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 146 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 147 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 148 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 149 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 150 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 151 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 152 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 153 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 154 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 155 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 156 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 157 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 158 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 159 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 160 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 161 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 162 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 163 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 164 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 165 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 166 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 167 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 168 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 169 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 170 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 171 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 172 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 173 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 174 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 175 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 176 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 177 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 178 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 179 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 180 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 181 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 182 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 183 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 184 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 185 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 186 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 187 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 188 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 189 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 190 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 191 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 192 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 193 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 194 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 195 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 196 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 197 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 198 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 199 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 200 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 201 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 202 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 203 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 204 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 205 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 206 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 207 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 208 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 209 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 210 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 211 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 212 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 213 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 214 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 215 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 216 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 217 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 218 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 219 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 220 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 221 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 222 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 223 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 224 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 225 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 226 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 227 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 228 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 229 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 230 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 231 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 232 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 233 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 234 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 235 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 236 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 237 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 238 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 239 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 240 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 241 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 242 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 243 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 244 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 245 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 246 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 247 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 248 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 249 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1, NesPpu::evaluateSprite},
    /* 250 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchNametableByte2, NesPpu::evaluateSprite},
    /* 251 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1, NesPpu::evaluateSprite},
    /* 252 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2, NesPpu::evaluateSprite},
    /* 253 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1, NesPpu::evaluateSprite},
    /* 254 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2, NesPpu::evaluateSprite},
    /* 255 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1, NesPpu::evaluateSprite},
    /* 256 */ {NesPpu::renderPixel, NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::incrementVertical, NesPpu::reloadShiftRegisters, NesPpu::evaluateSprite},
    /* 257 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 258 */ {NesPpu::fetchNametableByte2},
    /* 259 */ {NesPpu::fetchNametableByte1},
    /* 260 */ {NesPpu::fetchNametableByte2},
    /* 261 */ {NesPpu::fetchSprite0PatternByteLo1},
    /* 262 */ {NesPpu::fetchSprite0PatternByteLo2},
    /* 263 */ {NesPpu::fetchSprite0PatternByteHi1},
    /* 264 */ {NesPpu::fetchSprite0PatternByteHi2},
    /* 265 */ {NesPpu::fetchNametableByte1},
    /* 266 */ {NesPpu::fetchNametableByte2},
    /* 267 */ {NesPpu::fetchNametableByte1},
    /* 268 */ {NesPpu::fetchNametableByte2},
    /* 269 */ {NesPpu::fetchSprite1PatternByteLo1},
    /* 270 */ {NesPpu::fetchSprite1PatternByteLo2},
    /* 271 */ {NesPpu::fetchSprite1PatternByteHi1},
    /* 272 */ {NesPpu::fetchSprite1PatternByteHi2},
    /* 273 */ {NesPpu::fetchNametableByte1},
    /* 274 */ {NesPpu::fetchNametableByte2},
    /* 275 */ {NesPpu::fetchNametableByte1},
    /* 276 */ {NesPpu::fetchNametableByte2},
    /* 277 */ {NesPpu::fetchSprite2PatternByteLo1},
    /* 278 */ {NesPpu::fetchSprite2PatternByteLo2},
    /* 279 */ {NesPpu::fetchSprite2PatternByteHi1},
    /* 280 */ {NesPpu::fetchSprite2PatternByteHi2},
    /* 281 */ {NesPpu::fetchNametableByte1},
    /* 282 */ {NesPpu::fetchNametableByte2},
    /* 283 */ {NesPpu::fetchNametableByte1},
    /* 284 */ {NesPpu::fetchNametableByte2},
    /* 285 */ {NesPpu::fetchSprite3PatternByteLo1},
    /* 286 */ {NesPpu::fetchSprite3PatternByteLo2},
    /* 287 */ {NesPpu::fetchSprite3PatternByteHi1},
    /* 288 */ {NesPpu::fetchSprite3PatternByteHi2},
    /* 289 */ {NesPpu::fetchNametableByte1},
    /* 290 */ {NesPpu::fetchNametableByte2},
    /* 291 */ {NesPpu::fetchNametableByte1},
    /* 292 */ {NesPpu::fetchNametableByte2},
    /* 293 */ {NesPpu::fetchSprite4PatternByteLo1},
    /* 294 */ {NesPpu::fetchSprite4PatternByteLo2},
    /* 295 */ {NesPpu::fetchSprite4PatternByteHi1},
    /* 296 */ {NesPpu::fetchSprite4PatternByteHi2},
    /* 297 */ {NesPpu::fetchNametableByte1},
    /* 298 */ {NesPpu::fetchNametableByte2},
    /* 299 */ {NesPpu::fetchNametableByte1},
    /* 300 */ {NesPpu::fetchNametableByte2},
    /* 301 */ {NesPpu::fetchSprite5PatternByteLo1},
    /* 302 */ {NesPpu::fetchSprite5PatternByteLo2},
    /* 303 */ {NesPpu::fetchSprite5PatternByteHi1},
    /* 304 */ {NesPpu::fetchSprite5PatternByteHi2},
    /* 305 */ {NesPpu::fetchNametableByte1},
    /* 306 */ {NesPpu::fetchNametableByte2},
    /* 307 */ {NesPpu::fetchNametableByte1},
    /* 308 */ {NesPpu::fetchNametableByte2},
    /* 309 */ {NesPpu::fetchSprite6PatternByteLo1},
    /* 310 */ {NesPpu::fetchSprite6PatternByteLo2},
    /* 311 */ {NesPpu::fetchSprite6PatternByteHi1},
    /* 312 */ {NesPpu::fetchSprite6PatternByteHi2},
    /* 313 */ {NesPpu::fetchNametableByte1},
    /* 314 */ {NesPpu::fetchNametableByte2},
    /* 315 */ {NesPpu::fetchNametableByte1},
    /* 316 */ {NesPpu::fetchNametableByte2},
    /* 317 */ {NesPpu::fetchSprite7PatternByteLo1},
    /* 318 */ {NesPpu::fetchSprite7PatternByteLo2},
    /* 319 */ {NesPpu::fetchSprite7PatternByteHi1},
    /* 320 */ {NesPpu::fetchSprite7PatternByteHi2},
    /* 321 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 322 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 323 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 324 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 325 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 326 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 327 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 328 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 329 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 330 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 331 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 332 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 333 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 334 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 335 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 336 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 337 */ {NesPpu::fetchNametableByte1},
    /* 338 */ {NesPpu::fetchNametableByte2},
    /* 339 */ {NesPpu::fetchNametableByte1},
    /* 340 */ {NesPpu::fetchNametableByte2},
  };

  private static final TickFn[][] POST_RENDER_SCANLINE = {
    /*   0 */ {},
    /*   1 */ {},
    /*   2 */ {},
    /*   3 */ {},
    /*   4 */ {},
    /*   5 */ {},
    /*   6 */ {},
    /*   7 */ {},
    /*   8 */ {},
    /*   9 */ {},
    /*  10 */ {},
    /*  11 */ {},
    /*  12 */ {},
    /*  13 */ {},
    /*  14 */ {},
    /*  15 */ {},
    /*  16 */ {},
    /*  17 */ {},
    /*  18 */ {},
    /*  19 */ {},
    /*  20 */ {},
    /*  21 */ {},
    /*  22 */ {},
    /*  23 */ {},
    /*  24 */ {},
    /*  25 */ {},
    /*  26 */ {},
    /*  27 */ {},
    /*  28 */ {},
    /*  29 */ {},
    /*  30 */ {},
    /*  31 */ {},
    /*  32 */ {},
    /*  33 */ {},
    /*  34 */ {},
    /*  35 */ {},
    /*  36 */ {},
    /*  37 */ {},
    /*  38 */ {},
    /*  39 */ {},
    /*  40 */ {},
    /*  41 */ {},
    /*  42 */ {},
    /*  43 */ {},
    /*  44 */ {},
    /*  45 */ {},
    /*  46 */ {},
    /*  47 */ {},
    /*  48 */ {},
    /*  49 */ {},
    /*  50 */ {},
    /*  51 */ {},
    /*  52 */ {},
    /*  53 */ {},
    /*  54 */ {},
    /*  55 */ {},
    /*  56 */ {},
    /*  57 */ {},
    /*  58 */ {},
    /*  59 */ {},
    /*  60 */ {},
    /*  61 */ {},
    /*  62 */ {},
    /*  63 */ {},
    /*  64 */ {},
    /*  65 */ {},
    /*  66 */ {},
    /*  67 */ {},
    /*  68 */ {},
    /*  69 */ {},
    /*  70 */ {},
    /*  71 */ {},
    /*  72 */ {},
    /*  73 */ {},
    /*  74 */ {},
    /*  75 */ {},
    /*  76 */ {},
    /*  77 */ {},
    /*  78 */ {},
    /*  79 */ {},
    /*  80 */ {},
    /*  81 */ {},
    /*  82 */ {},
    /*  83 */ {},
    /*  84 */ {},
    /*  85 */ {},
    /*  86 */ {},
    /*  87 */ {},
    /*  88 */ {},
    /*  89 */ {},
    /*  90 */ {},
    /*  91 */ {},
    /*  92 */ {},
    /*  93 */ {},
    /*  94 */ {},
    /*  95 */ {},
    /*  96 */ {},
    /*  97 */ {},
    /*  98 */ {},
    /*  99 */ {},
    /* 100 */ {},
    /* 101 */ {},
    /* 102 */ {},
    /* 103 */ {},
    /* 104 */ {},
    /* 105 */ {},
    /* 106 */ {},
    /* 107 */ {},
    /* 108 */ {},
    /* 109 */ {},
    /* 110 */ {},
    /* 111 */ {},
    /* 112 */ {},
    /* 113 */ {},
    /* 114 */ {},
    /* 115 */ {},
    /* 116 */ {},
    /* 117 */ {},
    /* 118 */ {},
    /* 119 */ {},
    /* 120 */ {},
    /* 121 */ {},
    /* 122 */ {},
    /* 123 */ {},
    /* 124 */ {},
    /* 125 */ {},
    /* 126 */ {},
    /* 127 */ {},
    /* 128 */ {},
    /* 129 */ {},
    /* 130 */ {},
    /* 131 */ {},
    /* 132 */ {},
    /* 133 */ {},
    /* 134 */ {},
    /* 135 */ {},
    /* 136 */ {},
    /* 137 */ {},
    /* 138 */ {},
    /* 139 */ {},
    /* 140 */ {},
    /* 141 */ {},
    /* 142 */ {},
    /* 143 */ {},
    /* 144 */ {},
    /* 145 */ {},
    /* 146 */ {},
    /* 147 */ {},
    /* 148 */ {},
    /* 149 */ {},
    /* 150 */ {},
    /* 151 */ {},
    /* 152 */ {},
    /* 153 */ {},
    /* 154 */ {},
    /* 155 */ {},
    /* 156 */ {},
    /* 157 */ {},
    /* 158 */ {},
    /* 159 */ {},
    /* 160 */ {},
    /* 161 */ {},
    /* 162 */ {},
    /* 163 */ {},
    /* 164 */ {},
    /* 165 */ {},
    /* 166 */ {},
    /* 167 */ {},
    /* 168 */ {},
    /* 169 */ {},
    /* 170 */ {},
    /* 171 */ {},
    /* 172 */ {},
    /* 173 */ {},
    /* 174 */ {},
    /* 175 */ {},
    /* 176 */ {},
    /* 177 */ {},
    /* 178 */ {},
    /* 179 */ {},
    /* 180 */ {},
    /* 181 */ {},
    /* 182 */ {},
    /* 183 */ {},
    /* 184 */ {},
    /* 185 */ {},
    /* 186 */ {},
    /* 187 */ {},
    /* 188 */ {},
    /* 189 */ {},
    /* 190 */ {},
    /* 191 */ {},
    /* 192 */ {},
    /* 193 */ {},
    /* 194 */ {},
    /* 195 */ {},
    /* 196 */ {},
    /* 197 */ {},
    /* 198 */ {},
    /* 199 */ {},
    /* 200 */ {},
    /* 201 */ {},
    /* 202 */ {},
    /* 203 */ {},
    /* 204 */ {},
    /* 205 */ {},
    /* 206 */ {},
    /* 207 */ {},
    /* 208 */ {},
    /* 209 */ {},
    /* 210 */ {},
    /* 211 */ {},
    /* 212 */ {},
    /* 213 */ {},
    /* 214 */ {},
    /* 215 */ {},
    /* 216 */ {},
    /* 217 */ {},
    /* 218 */ {},
    /* 219 */ {},
    /* 220 */ {},
    /* 221 */ {},
    /* 222 */ {},
    /* 223 */ {},
    /* 224 */ {},
    /* 225 */ {},
    /* 226 */ {},
    /* 227 */ {},
    /* 228 */ {},
    /* 229 */ {},
    /* 230 */ {},
    /* 231 */ {},
    /* 232 */ {},
    /* 233 */ {},
    /* 234 */ {},
    /* 235 */ {},
    /* 236 */ {},
    /* 237 */ {},
    /* 238 */ {},
    /* 239 */ {},
    /* 240 */ {},
    /* 241 */ {},
    /* 242 */ {},
    /* 243 */ {},
    /* 244 */ {},
    /* 245 */ {},
    /* 246 */ {},
    /* 247 */ {},
    /* 248 */ {},
    /* 249 */ {},
    /* 250 */ {},
    /* 251 */ {},
    /* 252 */ {},
    /* 253 */ {},
    /* 254 */ {},
    /* 255 */ {},
    /* 256 */ {},
    /* 257 */ {},
    /* 258 */ {},
    /* 259 */ {},
    /* 260 */ {},
    /* 261 */ {},
    /* 262 */ {},
    /* 263 */ {},
    /* 264 */ {},
    /* 265 */ {},
    /* 266 */ {},
    /* 267 */ {},
    /* 268 */ {},
    /* 269 */ {},
    /* 270 */ {},
    /* 271 */ {},
    /* 272 */ {},
    /* 273 */ {},
    /* 274 */ {},
    /* 275 */ {},
    /* 276 */ {},
    /* 277 */ {},
    /* 278 */ {},
    /* 279 */ {},
    /* 280 */ {},
    /* 281 */ {},
    /* 282 */ {},
    /* 283 */ {},
    /* 284 */ {},
    /* 285 */ {},
    /* 286 */ {},
    /* 287 */ {},
    /* 288 */ {},
    /* 289 */ {},
    /* 290 */ {},
    /* 291 */ {},
    /* 292 */ {},
    /* 293 */ {},
    /* 294 */ {},
    /* 295 */ {},
    /* 296 */ {},
    /* 297 */ {},
    /* 298 */ {},
    /* 299 */ {},
    /* 300 */ {},
    /* 301 */ {},
    /* 302 */ {},
    /* 303 */ {},
    /* 304 */ {},
    /* 305 */ {},
    /* 306 */ {},
    /* 307 */ {},
    /* 308 */ {},
    /* 309 */ {},
    /* 310 */ {},
    /* 311 */ {},
    /* 312 */ {},
    /* 313 */ {},
    /* 314 */ {},
    /* 315 */ {},
    /* 316 */ {},
    /* 317 */ {},
    /* 318 */ {},
    /* 319 */ {},
    /* 320 */ {},
    /* 321 */ {},
    /* 322 */ {},
    /* 323 */ {},
    /* 324 */ {},
    /* 325 */ {},
    /* 326 */ {},
    /* 327 */ {},
    /* 328 */ {},
    /* 329 */ {},
    /* 330 */ {},
    /* 331 */ {},
    /* 332 */ {},
    /* 333 */ {},
    /* 334 */ {},
    /* 335 */ {},
    /* 336 */ {},
    /* 337 */ {},
    /* 338 */ {},
    /* 339 */ {},
    /* 340 */ {},
  };

  private static final TickFn[][] VBLANK_SCANLINE = {
    /*   0 */ {NesPpu::drawFrame, NesPpu::setVBlank0},
    /*   1 */ {NesPpu::setVBlank1},
    /*   2 */ {NesPpu::setVBlank2},
    /*   3 */ {NesPpu::setVBlank3},
    /*   4 */ {},
    /*   5 */ {},
    /*   6 */ {},
    /*   7 */ {},
    /*   8 */ {},
    /*   9 */ {},
    /*  10 */ {},
    /*  11 */ {},
    /*  12 */ {},
    /*  13 */ {},
    /*  14 */ {},
    /*  15 */ {},
    /*  16 */ {},
    /*  17 */ {},
    /*  18 */ {},
    /*  19 */ {},
    /*  20 */ {},
    /*  21 */ {},
    /*  22 */ {},
    /*  23 */ {},
    /*  24 */ {},
    /*  25 */ {},
    /*  26 */ {},
    /*  27 */ {},
    /*  28 */ {},
    /*  29 */ {},
    /*  30 */ {},
    /*  31 */ {},
    /*  32 */ {},
    /*  33 */ {},
    /*  34 */ {},
    /*  35 */ {},
    /*  36 */ {},
    /*  37 */ {},
    /*  38 */ {},
    /*  39 */ {},
    /*  40 */ {},
    /*  41 */ {},
    /*  42 */ {},
    /*  43 */ {},
    /*  44 */ {},
    /*  45 */ {},
    /*  46 */ {},
    /*  47 */ {},
    /*  48 */ {},
    /*  49 */ {},
    /*  50 */ {},
    /*  51 */ {},
    /*  52 */ {},
    /*  53 */ {},
    /*  54 */ {},
    /*  55 */ {},
    /*  56 */ {},
    /*  57 */ {},
    /*  58 */ {},
    /*  59 */ {},
    /*  60 */ {},
    /*  61 */ {},
    /*  62 */ {},
    /*  63 */ {},
    /*  64 */ {},
    /*  65 */ {},
    /*  66 */ {},
    /*  67 */ {},
    /*  68 */ {},
    /*  69 */ {},
    /*  70 */ {},
    /*  71 */ {},
    /*  72 */ {},
    /*  73 */ {},
    /*  74 */ {},
    /*  75 */ {},
    /*  76 */ {},
    /*  77 */ {},
    /*  78 */ {},
    /*  79 */ {},
    /*  80 */ {},
    /*  81 */ {},
    /*  82 */ {},
    /*  83 */ {},
    /*  84 */ {},
    /*  85 */ {},
    /*  86 */ {},
    /*  87 */ {},
    /*  88 */ {},
    /*  89 */ {},
    /*  90 */ {},
    /*  91 */ {},
    /*  92 */ {},
    /*  93 */ {},
    /*  94 */ {},
    /*  95 */ {},
    /*  96 */ {},
    /*  97 */ {},
    /*  98 */ {},
    /*  99 */ {},
    /* 100 */ {},
    /* 101 */ {},
    /* 102 */ {},
    /* 103 */ {},
    /* 104 */ {},
    /* 105 */ {},
    /* 106 */ {},
    /* 107 */ {},
    /* 108 */ {},
    /* 109 */ {},
    /* 110 */ {},
    /* 111 */ {},
    /* 112 */ {},
    /* 113 */ {},
    /* 114 */ {},
    /* 115 */ {},
    /* 116 */ {},
    /* 117 */ {},
    /* 118 */ {},
    /* 119 */ {},
    /* 120 */ {},
    /* 121 */ {},
    /* 122 */ {},
    /* 123 */ {},
    /* 124 */ {},
    /* 125 */ {},
    /* 126 */ {},
    /* 127 */ {},
    /* 128 */ {},
    /* 129 */ {},
    /* 130 */ {},
    /* 131 */ {},
    /* 132 */ {},
    /* 133 */ {},
    /* 134 */ {},
    /* 135 */ {},
    /* 136 */ {},
    /* 137 */ {},
    /* 138 */ {},
    /* 139 */ {},
    /* 140 */ {},
    /* 141 */ {},
    /* 142 */ {},
    /* 143 */ {},
    /* 144 */ {},
    /* 145 */ {},
    /* 146 */ {},
    /* 147 */ {},
    /* 148 */ {},
    /* 149 */ {},
    /* 150 */ {},
    /* 151 */ {},
    /* 152 */ {},
    /* 153 */ {},
    /* 154 */ {},
    /* 155 */ {},
    /* 156 */ {},
    /* 157 */ {},
    /* 158 */ {},
    /* 159 */ {},
    /* 160 */ {},
    /* 161 */ {},
    /* 162 */ {},
    /* 163 */ {},
    /* 164 */ {},
    /* 165 */ {},
    /* 166 */ {},
    /* 167 */ {},
    /* 168 */ {},
    /* 169 */ {},
    /* 170 */ {},
    /* 171 */ {},
    /* 172 */ {},
    /* 173 */ {},
    /* 174 */ {},
    /* 175 */ {},
    /* 176 */ {},
    /* 177 */ {},
    /* 178 */ {},
    /* 179 */ {},
    /* 180 */ {},
    /* 181 */ {},
    /* 182 */ {},
    /* 183 */ {},
    /* 184 */ {},
    /* 185 */ {},
    /* 186 */ {},
    /* 187 */ {},
    /* 188 */ {},
    /* 189 */ {},
    /* 190 */ {},
    /* 191 */ {},
    /* 192 */ {},
    /* 193 */ {},
    /* 194 */ {},
    /* 195 */ {},
    /* 196 */ {},
    /* 197 */ {},
    /* 198 */ {},
    /* 199 */ {},
    /* 200 */ {},
    /* 201 */ {},
    /* 202 */ {},
    /* 203 */ {},
    /* 204 */ {},
    /* 205 */ {},
    /* 206 */ {},
    /* 207 */ {},
    /* 208 */ {},
    /* 209 */ {},
    /* 210 */ {},
    /* 211 */ {},
    /* 212 */ {},
    /* 213 */ {},
    /* 214 */ {},
    /* 215 */ {},
    /* 216 */ {},
    /* 217 */ {},
    /* 218 */ {},
    /* 219 */ {},
    /* 220 */ {},
    /* 221 */ {},
    /* 222 */ {},
    /* 223 */ {},
    /* 224 */ {},
    /* 225 */ {},
    /* 226 */ {},
    /* 227 */ {},
    /* 228 */ {},
    /* 229 */ {},
    /* 230 */ {},
    /* 231 */ {},
    /* 232 */ {},
    /* 233 */ {},
    /* 234 */ {},
    /* 235 */ {},
    /* 236 */ {},
    /* 237 */ {},
    /* 238 */ {},
    /* 239 */ {},
    /* 240 */ {},
    /* 241 */ {},
    /* 242 */ {},
    /* 243 */ {},
    /* 244 */ {},
    /* 245 */ {},
    /* 246 */ {},
    /* 247 */ {},
    /* 248 */ {},
    /* 249 */ {},
    /* 250 */ {},
    /* 251 */ {},
    /* 252 */ {},
    /* 253 */ {},
    /* 254 */ {},
    /* 255 */ {},
    /* 256 */ {},
    /* 257 */ {},
    /* 258 */ {},
    /* 259 */ {},
    /* 260 */ {},
    /* 261 */ {},
    /* 262 */ {},
    /* 263 */ {},
    /* 264 */ {},
    /* 265 */ {},
    /* 266 */ {},
    /* 267 */ {},
    /* 268 */ {},
    /* 269 */ {},
    /* 270 */ {},
    /* 271 */ {},
    /* 272 */ {},
    /* 273 */ {},
    /* 274 */ {},
    /* 275 */ {},
    /* 276 */ {},
    /* 277 */ {},
    /* 278 */ {},
    /* 279 */ {},
    /* 280 */ {},
    /* 281 */ {},
    /* 282 */ {},
    /* 283 */ {},
    /* 284 */ {},
    /* 285 */ {},
    /* 286 */ {},
    /* 287 */ {},
    /* 288 */ {},
    /* 289 */ {},
    /* 290 */ {},
    /* 291 */ {},
    /* 292 */ {},
    /* 293 */ {},
    /* 294 */ {},
    /* 295 */ {},
    /* 296 */ {},
    /* 297 */ {},
    /* 298 */ {},
    /* 299 */ {},
    /* 300 */ {},
    /* 301 */ {},
    /* 302 */ {},
    /* 303 */ {},
    /* 304 */ {},
    /* 305 */ {},
    /* 306 */ {},
    /* 307 */ {},
    /* 308 */ {},
    /* 309 */ {},
    /* 310 */ {},
    /* 311 */ {},
    /* 312 */ {},
    /* 313 */ {},
    /* 314 */ {},
    /* 315 */ {},
    /* 316 */ {},
    /* 317 */ {},
    /* 318 */ {},
    /* 319 */ {},
    /* 320 */ {},
    /* 321 */ {},
    /* 322 */ {},
    /* 323 */ {},
    /* 324 */ {},
    /* 325 */ {},
    /* 326 */ {},
    /* 327 */ {},
    /* 328 */ {},
    /* 329 */ {},
    /* 330 */ {},
    /* 331 */ {},
    /* 332 */ {},
    /* 333 */ {},
    /* 334 */ {},
    /* 335 */ {},
    /* 336 */ {},
    /* 337 */ {},
    /* 338 */ {},
    /* 339 */ {},
    /* 340 */ {},
  };

  private static final TickFn[][] PRE_RENDER_SCANLINE = {
    /*   0 */ {NesPpu::clearSprite0Hit},
    /*   1 */ {NesPpu::clearVBlank, NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*   2 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*   3 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*   4 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*   5 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*   6 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*   7 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*   8 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*   9 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  10 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  11 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  12 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  13 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  14 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  15 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  16 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  17 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  18 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  19 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  20 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  21 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  22 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  23 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  24 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  25 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  26 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  27 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  28 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  29 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  30 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  31 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  32 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  33 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  34 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  35 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  36 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  37 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  38 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  39 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  40 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  41 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  42 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  43 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  44 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  45 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  46 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  47 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  48 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  49 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  50 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  51 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  52 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  53 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  54 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  55 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  56 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  57 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  58 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  59 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  60 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  61 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  62 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  63 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  64 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  65 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  66 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  67 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  68 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  69 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  70 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  71 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  72 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  73 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  74 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  75 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  76 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  77 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  78 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  79 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  80 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  81 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  82 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  83 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  84 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  85 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  86 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  87 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  88 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  89 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  90 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  91 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /*  92 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /*  93 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /*  94 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /*  95 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /*  96 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /*  97 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /*  98 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /*  99 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 100 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 101 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 102 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 103 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 104 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 105 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 106 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 107 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 108 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 109 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 110 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 111 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 112 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 113 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 114 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 115 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 116 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 117 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 118 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 119 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 120 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 121 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 122 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 123 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 124 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 125 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 126 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 127 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 128 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 129 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 130 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 131 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 132 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 133 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 134 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 135 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 136 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 137 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 138 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 139 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 140 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 141 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 142 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 143 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 144 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 145 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 146 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 147 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 148 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 149 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 150 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 151 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 152 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 153 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 154 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 155 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 156 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 157 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 158 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 159 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 160 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 161 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 162 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 163 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 164 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 165 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 166 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 167 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 168 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 169 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 170 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 171 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 172 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 173 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 174 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 175 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 176 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 177 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 178 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 179 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 180 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 181 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 182 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 183 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 184 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 185 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 186 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 187 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 188 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 189 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 190 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 191 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 192 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 193 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 194 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 195 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 196 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 197 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 198 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 199 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 200 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 201 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 202 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 203 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 204 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 205 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 206 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 207 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 208 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 209 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 210 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 211 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 212 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 213 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 214 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 215 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 216 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 217 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 218 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 219 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 220 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 221 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 222 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 223 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 224 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 225 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 226 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 227 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 228 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 229 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 230 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 231 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 232 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 233 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 234 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 235 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 236 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 237 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 238 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 239 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 240 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 241 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 242 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 243 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 244 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 245 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 246 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 247 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 248 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 249 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 250 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 251 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 252 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 253 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 254 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 255 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 256 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::incrementVertical, NesPpu::reloadShiftRegisters},
    /* 257 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 258 */ {NesPpu::fetchNametableByte2},
    /* 259 */ {NesPpu::fetchNametableByte1},
    /* 260 */ {NesPpu::fetchNametableByte2},
    /* 261 */ {}, // TODO: sprite
    /* 262 */ {}, // TODO: sprite
    /* 263 */ {}, // TODO: sprite
    /* 264 */ {}, // TODO: sprite
    /* 265 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 266 */ {NesPpu::fetchNametableByte2},
    /* 267 */ {NesPpu::fetchNametableByte1},
    /* 268 */ {NesPpu::fetchNametableByte2},
    /* 269 */ {}, // TODO: sprite
    /* 270 */ {}, // TODO: sprite
    /* 271 */ {}, // TODO: sprite
    /* 272 */ {}, // TODO: sprite
    /* 273 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 274 */ {NesPpu::fetchNametableByte2},
    /* 275 */ {NesPpu::fetchNametableByte1},
    /* 276 */ {NesPpu::fetchNametableByte2},
    /* 277 */ {}, // TODO: sprite
    /* 278 */ {}, // TODO: sprite
    /* 279 */ {}, // TODO: sprite
    /* 280 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 281 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 282 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 283 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 284 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 285 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 286 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 287 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 288 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 289 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 290 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 291 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 292 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 293 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 294 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 295 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 296 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 297 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 298 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 299 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte1},
    /* 300 */ {NesPpu::reloadVertical, NesPpu::fetchNametableByte2},
    /* 301 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 302 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 303 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 304 */ {NesPpu::reloadVertical}, // TODO: sprite
    /* 305 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 306 */ {NesPpu::fetchNametableByte2},
    /* 307 */ {NesPpu::fetchNametableByte1},
    /* 308 */ {NesPpu::fetchNametableByte2},
    /* 309 */ {}, // TODO: sprite
    /* 310 */ {}, // TODO: sprite
    /* 311 */ {}, // TODO: sprite
    /* 312 */ {}, // TODO: sprite
    /* 313 */ {NesPpu::reloadHorizontal, NesPpu::fetchNametableByte1},
    /* 314 */ {NesPpu::fetchNametableByte2},
    /* 315 */ {NesPpu::fetchNametableByte1},
    /* 316 */ {NesPpu::fetchNametableByte2},
    /* 317 */ {}, // TODO: sprite
    /* 318 */ {}, // TODO: sprite
    /* 319 */ {}, // TODO: sprite
    /* 320 */ {}, // TODO: sprite
    /* 321 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 322 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 323 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 324 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 325 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 326 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 327 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 328 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 329 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte1},
    /* 330 */ {NesPpu::shiftRegisters, NesPpu::fetchNametableByte2},
    /* 331 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte1},
    /* 332 */ {NesPpu::shiftRegisters, NesPpu::fetchAttributeByte2},
    /* 333 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo1},
    /* 334 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteLo2},
    /* 335 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi1},
    /* 336 */ {NesPpu::shiftRegisters, NesPpu::fetchPatternByteHi2, NesPpu::incrementHorizontal, NesPpu::reloadShiftRegisters},
    /* 337 */ {NesPpu::fetchNametableByte1},
    /* 338 */ {NesPpu::fetchNametableByte2},
    /* 339 */ {NesPpu::fetchNametableByte1},
    /* 340 */ {NesPpu::fetchNametableByte2},
  };

  private static final TickFn[][][] SCANLINES = {
    /*   0 */ VISIBLE_SCANLINE,
    /*   1 */ VISIBLE_SCANLINE,
    /*   2 */ VISIBLE_SCANLINE,
    /*   3 */ VISIBLE_SCANLINE,
    /*   4 */ VISIBLE_SCANLINE,
    /*   5 */ VISIBLE_SCANLINE,
    /*   6 */ VISIBLE_SCANLINE,
    /*   7 */ VISIBLE_SCANLINE,
    /*   8 */ VISIBLE_SCANLINE,
    /*   9 */ VISIBLE_SCANLINE,
    /*  10 */ VISIBLE_SCANLINE,
    /*  11 */ VISIBLE_SCANLINE,
    /*  12 */ VISIBLE_SCANLINE,
    /*  13 */ VISIBLE_SCANLINE,
    /*  14 */ VISIBLE_SCANLINE,
    /*  15 */ VISIBLE_SCANLINE,
    /*  16 */ VISIBLE_SCANLINE,
    /*  17 */ VISIBLE_SCANLINE,
    /*  18 */ VISIBLE_SCANLINE,
    /*  19 */ VISIBLE_SCANLINE,
    /*  20 */ VISIBLE_SCANLINE,
    /*  21 */ VISIBLE_SCANLINE,
    /*  22 */ VISIBLE_SCANLINE,
    /*  23 */ VISIBLE_SCANLINE,
    /*  24 */ VISIBLE_SCANLINE,
    /*  25 */ VISIBLE_SCANLINE,
    /*  26 */ VISIBLE_SCANLINE,
    /*  27 */ VISIBLE_SCANLINE,
    /*  28 */ VISIBLE_SCANLINE,
    /*  29 */ VISIBLE_SCANLINE,
    /*  30 */ VISIBLE_SCANLINE,
    /*  31 */ VISIBLE_SCANLINE,
    /*  32 */ VISIBLE_SCANLINE,
    /*  33 */ VISIBLE_SCANLINE,
    /*  34 */ VISIBLE_SCANLINE,
    /*  35 */ VISIBLE_SCANLINE,
    /*  36 */ VISIBLE_SCANLINE,
    /*  37 */ VISIBLE_SCANLINE,
    /*  38 */ VISIBLE_SCANLINE,
    /*  39 */ VISIBLE_SCANLINE,
    /*  40 */ VISIBLE_SCANLINE,
    /*  41 */ VISIBLE_SCANLINE,
    /*  42 */ VISIBLE_SCANLINE,
    /*  43 */ VISIBLE_SCANLINE,
    /*  44 */ VISIBLE_SCANLINE,
    /*  45 */ VISIBLE_SCANLINE,
    /*  46 */ VISIBLE_SCANLINE,
    /*  47 */ VISIBLE_SCANLINE,
    /*  48 */ VISIBLE_SCANLINE,
    /*  49 */ VISIBLE_SCANLINE,
    /*  50 */ VISIBLE_SCANLINE,
    /*  51 */ VISIBLE_SCANLINE,
    /*  52 */ VISIBLE_SCANLINE,
    /*  53 */ VISIBLE_SCANLINE,
    /*  54 */ VISIBLE_SCANLINE,
    /*  55 */ VISIBLE_SCANLINE,
    /*  56 */ VISIBLE_SCANLINE,
    /*  57 */ VISIBLE_SCANLINE,
    /*  58 */ VISIBLE_SCANLINE,
    /*  59 */ VISIBLE_SCANLINE,
    /*  60 */ VISIBLE_SCANLINE,
    /*  61 */ VISIBLE_SCANLINE,
    /*  62 */ VISIBLE_SCANLINE,
    /*  63 */ VISIBLE_SCANLINE,
    /*  64 */ VISIBLE_SCANLINE,
    /*  65 */ VISIBLE_SCANLINE,
    /*  66 */ VISIBLE_SCANLINE,
    /*  67 */ VISIBLE_SCANLINE,
    /*  68 */ VISIBLE_SCANLINE,
    /*  69 */ VISIBLE_SCANLINE,
    /*  70 */ VISIBLE_SCANLINE,
    /*  71 */ VISIBLE_SCANLINE,
    /*  72 */ VISIBLE_SCANLINE,
    /*  73 */ VISIBLE_SCANLINE,
    /*  74 */ VISIBLE_SCANLINE,
    /*  75 */ VISIBLE_SCANLINE,
    /*  76 */ VISIBLE_SCANLINE,
    /*  77 */ VISIBLE_SCANLINE,
    /*  78 */ VISIBLE_SCANLINE,
    /*  79 */ VISIBLE_SCANLINE,
    /*  80 */ VISIBLE_SCANLINE,
    /*  81 */ VISIBLE_SCANLINE,
    /*  82 */ VISIBLE_SCANLINE,
    /*  83 */ VISIBLE_SCANLINE,
    /*  84 */ VISIBLE_SCANLINE,
    /*  85 */ VISIBLE_SCANLINE,
    /*  86 */ VISIBLE_SCANLINE,
    /*  87 */ VISIBLE_SCANLINE,
    /*  88 */ VISIBLE_SCANLINE,
    /*  89 */ VISIBLE_SCANLINE,
    /*  90 */ VISIBLE_SCANLINE,
    /*  91 */ VISIBLE_SCANLINE,
    /*  92 */ VISIBLE_SCANLINE,
    /*  93 */ VISIBLE_SCANLINE,
    /*  94 */ VISIBLE_SCANLINE,
    /*  95 */ VISIBLE_SCANLINE,
    /*  96 */ VISIBLE_SCANLINE,
    /*  97 */ VISIBLE_SCANLINE,
    /*  98 */ VISIBLE_SCANLINE,
    /*  99 */ VISIBLE_SCANLINE,
    /* 100 */ VISIBLE_SCANLINE,
    /* 101 */ VISIBLE_SCANLINE,
    /* 102 */ VISIBLE_SCANLINE,
    /* 103 */ VISIBLE_SCANLINE,
    /* 104 */ VISIBLE_SCANLINE,
    /* 105 */ VISIBLE_SCANLINE,
    /* 106 */ VISIBLE_SCANLINE,
    /* 107 */ VISIBLE_SCANLINE,
    /* 108 */ VISIBLE_SCANLINE,
    /* 109 */ VISIBLE_SCANLINE,
    /* 110 */ VISIBLE_SCANLINE,
    /* 111 */ VISIBLE_SCANLINE,
    /* 112 */ VISIBLE_SCANLINE,
    /* 113 */ VISIBLE_SCANLINE,
    /* 114 */ VISIBLE_SCANLINE,
    /* 115 */ VISIBLE_SCANLINE,
    /* 116 */ VISIBLE_SCANLINE,
    /* 117 */ VISIBLE_SCANLINE,
    /* 118 */ VISIBLE_SCANLINE,
    /* 119 */ VISIBLE_SCANLINE,
    /* 120 */ VISIBLE_SCANLINE,
    /* 121 */ VISIBLE_SCANLINE,
    /* 122 */ VISIBLE_SCANLINE,
    /* 123 */ VISIBLE_SCANLINE,
    /* 124 */ VISIBLE_SCANLINE,
    /* 125 */ VISIBLE_SCANLINE,
    /* 126 */ VISIBLE_SCANLINE,
    /* 127 */ VISIBLE_SCANLINE,
    /* 128 */ VISIBLE_SCANLINE,
    /* 129 */ VISIBLE_SCANLINE,
    /* 130 */ VISIBLE_SCANLINE,
    /* 131 */ VISIBLE_SCANLINE,
    /* 132 */ VISIBLE_SCANLINE,
    /* 133 */ VISIBLE_SCANLINE,
    /* 134 */ VISIBLE_SCANLINE,
    /* 135 */ VISIBLE_SCANLINE,
    /* 136 */ VISIBLE_SCANLINE,
    /* 137 */ VISIBLE_SCANLINE,
    /* 138 */ VISIBLE_SCANLINE,
    /* 139 */ VISIBLE_SCANLINE,
    /* 140 */ VISIBLE_SCANLINE,
    /* 141 */ VISIBLE_SCANLINE,
    /* 142 */ VISIBLE_SCANLINE,
    /* 143 */ VISIBLE_SCANLINE,
    /* 144 */ VISIBLE_SCANLINE,
    /* 145 */ VISIBLE_SCANLINE,
    /* 146 */ VISIBLE_SCANLINE,
    /* 147 */ VISIBLE_SCANLINE,
    /* 148 */ VISIBLE_SCANLINE,
    /* 149 */ VISIBLE_SCANLINE,
    /* 150 */ VISIBLE_SCANLINE,
    /* 151 */ VISIBLE_SCANLINE,
    /* 152 */ VISIBLE_SCANLINE,
    /* 153 */ VISIBLE_SCANLINE,
    /* 154 */ VISIBLE_SCANLINE,
    /* 155 */ VISIBLE_SCANLINE,
    /* 156 */ VISIBLE_SCANLINE,
    /* 157 */ VISIBLE_SCANLINE,
    /* 158 */ VISIBLE_SCANLINE,
    /* 159 */ VISIBLE_SCANLINE,
    /* 160 */ VISIBLE_SCANLINE,
    /* 161 */ VISIBLE_SCANLINE,
    /* 162 */ VISIBLE_SCANLINE,
    /* 163 */ VISIBLE_SCANLINE,
    /* 164 */ VISIBLE_SCANLINE,
    /* 165 */ VISIBLE_SCANLINE,
    /* 166 */ VISIBLE_SCANLINE,
    /* 167 */ VISIBLE_SCANLINE,
    /* 168 */ VISIBLE_SCANLINE,
    /* 169 */ VISIBLE_SCANLINE,
    /* 170 */ VISIBLE_SCANLINE,
    /* 171 */ VISIBLE_SCANLINE,
    /* 172 */ VISIBLE_SCANLINE,
    /* 173 */ VISIBLE_SCANLINE,
    /* 174 */ VISIBLE_SCANLINE,
    /* 175 */ VISIBLE_SCANLINE,
    /* 176 */ VISIBLE_SCANLINE,
    /* 177 */ VISIBLE_SCANLINE,
    /* 178 */ VISIBLE_SCANLINE,
    /* 179 */ VISIBLE_SCANLINE,
    /* 180 */ VISIBLE_SCANLINE,
    /* 181 */ VISIBLE_SCANLINE,
    /* 182 */ VISIBLE_SCANLINE,
    /* 183 */ VISIBLE_SCANLINE,
    /* 184 */ VISIBLE_SCANLINE,
    /* 185 */ VISIBLE_SCANLINE,
    /* 186 */ VISIBLE_SCANLINE,
    /* 187 */ VISIBLE_SCANLINE,
    /* 188 */ VISIBLE_SCANLINE,
    /* 189 */ VISIBLE_SCANLINE,
    /* 190 */ VISIBLE_SCANLINE,
    /* 191 */ VISIBLE_SCANLINE,
    /* 192 */ VISIBLE_SCANLINE,
    /* 193 */ VISIBLE_SCANLINE,
    /* 194 */ VISIBLE_SCANLINE,
    /* 195 */ VISIBLE_SCANLINE,
    /* 196 */ VISIBLE_SCANLINE,
    /* 197 */ VISIBLE_SCANLINE,
    /* 198 */ VISIBLE_SCANLINE,
    /* 199 */ VISIBLE_SCANLINE,
    /* 200 */ VISIBLE_SCANLINE,
    /* 201 */ VISIBLE_SCANLINE,
    /* 202 */ VISIBLE_SCANLINE,
    /* 203 */ VISIBLE_SCANLINE,
    /* 204 */ VISIBLE_SCANLINE,
    /* 205 */ VISIBLE_SCANLINE,
    /* 206 */ VISIBLE_SCANLINE,
    /* 207 */ VISIBLE_SCANLINE,
    /* 208 */ VISIBLE_SCANLINE,
    /* 209 */ VISIBLE_SCANLINE,
    /* 210 */ VISIBLE_SCANLINE,
    /* 211 */ VISIBLE_SCANLINE,
    /* 212 */ VISIBLE_SCANLINE,
    /* 213 */ VISIBLE_SCANLINE,
    /* 214 */ VISIBLE_SCANLINE,
    /* 215 */ VISIBLE_SCANLINE,
    /* 216 */ VISIBLE_SCANLINE,
    /* 217 */ VISIBLE_SCANLINE,
    /* 218 */ VISIBLE_SCANLINE,
    /* 219 */ VISIBLE_SCANLINE,
    /* 220 */ VISIBLE_SCANLINE,
    /* 221 */ VISIBLE_SCANLINE,
    /* 222 */ VISIBLE_SCANLINE,
    /* 223 */ VISIBLE_SCANLINE,
    /* 224 */ VISIBLE_SCANLINE,
    /* 225 */ VISIBLE_SCANLINE,
    /* 226 */ VISIBLE_SCANLINE,
    /* 227 */ VISIBLE_SCANLINE,
    /* 228 */ VISIBLE_SCANLINE,
    /* 229 */ VISIBLE_SCANLINE,
    /* 230 */ VISIBLE_SCANLINE,
    /* 231 */ VISIBLE_SCANLINE,
    /* 232 */ VISIBLE_SCANLINE,
    /* 233 */ VISIBLE_SCANLINE,
    /* 234 */ VISIBLE_SCANLINE,
    /* 235 */ VISIBLE_SCANLINE,
    /* 236 */ VISIBLE_SCANLINE,
    /* 237 */ VISIBLE_SCANLINE,
    /* 238 */ VISIBLE_SCANLINE,
    /* 239 */ VISIBLE_SCANLINE,
    /* 240 */ POST_RENDER_SCANLINE,
    /* 241 */ VBLANK_SCANLINE,
    /* 242 */ POST_RENDER_SCANLINE,
    /* 243 */ POST_RENDER_SCANLINE,
    /* 244 */ POST_RENDER_SCANLINE,
    /* 245 */ POST_RENDER_SCANLINE,
    /* 246 */ POST_RENDER_SCANLINE,
    /* 247 */ POST_RENDER_SCANLINE,
    /* 248 */ POST_RENDER_SCANLINE,
    /* 249 */ POST_RENDER_SCANLINE,
    /* 250 */ POST_RENDER_SCANLINE,
    /* 251 */ POST_RENDER_SCANLINE,
    /* 252 */ POST_RENDER_SCANLINE,
    /* 253 */ POST_RENDER_SCANLINE,
    /* 254 */ POST_RENDER_SCANLINE,
    /* 255 */ POST_RENDER_SCANLINE,
    /* 256 */ POST_RENDER_SCANLINE,
    /* 257 */ POST_RENDER_SCANLINE,
    /* 258 */ POST_RENDER_SCANLINE,
    /* 259 */ POST_RENDER_SCANLINE,
    /* 260 */ POST_RENDER_SCANLINE,
    /* 261 */ PRE_RENDER_SCANLINE,
  };

  private final NesMapper mapper;
  private final Display display;
  private final byte[] ram;
  private final NesPpuOam oam;
  private final NesPpuPalette palette;
  private final byte[] paletteIndexes;
  private final byte[] frame = new byte[256 * 240 * 3];

  private int scanline = 0;
  private int dot = 0;
  private boolean odd = false;

  private byte buffer = 0;

  private byte ctrl = 0;
  private byte mask = 0;
  private byte status = 0;

  private short v = 0;
  private short t = 0;
  private byte x = 0;
  private byte w = 0;

  private final NesPpuNametableByte nametableByte = new NesPpuNametableByte();
  private final NesPpuAttributeByte attributeByte = new NesPpuAttributeByte();
  private final NesPpuPatternByte patternLoByte = new NesPpuPatternByte();
  private final NesPpuPatternByte patternHiByte = new NesPpuPatternByte();

  private boolean vblPending = false;
  private boolean nmiPending = false;
  private boolean nmi = false;

  public NesPpu(NesMapper mapper, Display display, NesPpuPalette palette) {
    this.mapper = mapper;
    this.display = display;
    this.ram = new byte[0x2000];
    this.oam = new NesPpuOam();
    this.paletteIndexes = new byte[0x20];
    this.palette = palette;
  }

  public void writeControl(byte data) {
    ctrl = data;
    if (isNmiEnabled()) {
      nmi = isVblEnabled();
    } else {
      nmiPending = false;
    }
    /*
    t: ...GH.. ........ <- d: ......GH
       <used elsewhere> <- d: ABCDEF..
    */
    t = (short) ((t & 0b0111_0011_1111_1111) | ((Byte.toUnsignedInt(data) & 0b0000_0011) << 10));
  }

  public byte readMask() {
    return mask;
  }

  public void writeMask(byte data) {
    mask = data;
  }

  public byte readStatus() {
    byte result = status;
    status &= 0b0111_1111;
    vblPending = false;
    nmiPending = false;
    w = 0;
    return result;
  }

  public void writeOamAddr(byte data) {
    oam.setAddress(data);
  }

  public byte readOamData() {
    return oam.get();
  }

  public void writeOamData(byte data) {
    oam.put(data);
  }

  public void writeScroll(byte data) {
    if (w == 0) {
      /*
      t: ....... ...ABCDE <- d: ABCDE...
      x:              FGH <- d: .....FGH
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0111_1111_1110_0000)
                  | (byte) ((Byte.toUnsignedInt(data) & 0b0001_1111) >>> 3));
      x = (byte) (Byte.toUnsignedInt(data) & 0b0000_0111);
      w = 1;
    } else {
      /*
      t: FGH..AB CDE..... <- d: ABCDEFGH
      w:                  <- 0
      */
      t =
          (short)
              ((t & 0b0000_1100_0001_1111)
                  | ((Byte.toUnsignedInt(data) & 0b1111_1000) << 2)
                  | ((Byte.toUnsignedInt(data) & 0b0000_0111) << 12));
      w = 0;
    }
  }

  public void writeAddress(byte data) {
    if (w == 0) {
      /*
      t: .CDEFGH ........ <- d: ..CDEFGH
             <unused>     <- d: AB......
      t: Z...... ........ <- 0 (bit Z is cleared)
      w:                  <- 1
      */
      t =
          (short)
              ((t & 0b0000_0000_1111_1111)
                  | (short) ((Byte.toUnsignedInt(data) << 8) & 0b0011_1111_0000_0000));
      w = 1;
    } else {
      /*
      t: ....... ABCDEFGH <- d: ABCDEFGH
      v: <...all bits...> <- t: <...all bits...>
      w:                  <- 0
      */
      t = (short) ((t & 0b0111_1111_0000_0000) | Byte.toUnsignedInt(data));
      v = t;
      w = 0;
    }
  }

  public byte readData() {
    byte result;
    if (0 <= v && v < 0x2000) {
      result = buffer;
      buffer = mapper.readChr(v);
      incrementAddress();
      return result;
    }
    if (0x2000 <= v && v < 0x3000) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3000 <= v && v < 0x3f00) {
      result = buffer;
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    if (0x3f00 <= v && v < 0x4000) {
      result = paletteIndexes[getPaletteAddress(v)];
      buffer = ram[mapper.getNametableMirrorAddress(v)];
      incrementAddress();
      return result;
    }
    throw new IllegalArgumentException("cannot read from PPU address " + v);
  }

  public void writeData(byte data) {
    if (0 <= v && v < 0x2000) {
      mapper.writeChr(v, data);
      incrementAddress();
      return;
    }
    if (0x2000 <= v && v < 0x3000) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3000 <= v && v < 0x3f00) {
      ram[mapper.getNametableMirrorAddress(v)] = data;
      incrementAddress();
      return;
    }
    if (0x3f00 <= v && v < 0x4000) {
      paletteIndexes[getPaletteAddress(v)] = data;
      incrementAddress();
      return;
    }
    throw new IllegalArgumentException("cannot write to PPU address " + v);
  }

  public boolean tick() {
    Arrays.stream(SCANLINES[scanline][dot]).forEach(fn -> fn.accept(this));
    advanceDot();
    boolean nmi = this.nmi;
    this.nmi = false;
    return nmi;
  }

  private void advanceDot() {
    assert dot >= 0 && dot <= 340;
    if (dot == 340) {
      dot = 0;
      advanceScanline();
      return;
    }
    dot++;
  }

  private void advanceScanline() {
    assert scanline >= 0 && scanline <= 261;
    if (scanline == 261) {
      scanline = 0;
      advanceFrame();
      return;
    }
    scanline++;
  }

  private void advanceFrame() {
    if (isRenderingEnabled() && odd) {
      dot = 1;
    }
    odd = !odd;
  }

  private void drawFrame() {
    if (!isRenderingEnabled()) {
      return;
    }
    display.draw(frame);
  }

  private int getPaletteAddress(short address) {
    int addr = address & 0b0000_0000_0001_1111;
    return addr == 0x10 ? 0x0 : addr;
  }

  private void incrementAddress() {
    if (isVramIncrementVertical()) {
      v += 32;
    } else {
      v += 1;
    }
  }

  // https://www.nesdev.org/wiki/PPU_scrolling#Wrapping_around

  private void incrementHorizontal() {
    if (!isRenderingEnabled()) {
      return;
    }

    int coarseX = (v & 0b0000_0000_0001_1111);

    coarseX++;

    if (coarseX == 32) {
      // Coarse X wraps around. Set it to 0 and switch horizontal nametable.
      coarseX = 0;
      v ^= 0b0000_0100_0000_0000;
    }

    v &= 0b0111_1111_1110_0000;
    v |= (short) coarseX;
  }

  private void incrementVertical() {
    if (!isRenderingEnabled()) {
      return;
    }

    int fineY = (v & 0b0111_0000_0000_0000) >>> 12;
    int coarseY = (v & 0b0000_0011_1110_0000) >>> 5;

    fineY = (fineY + 1) % 8;

    if (fineY == 0) {
      coarseY++;
      if (coarseY == 30) {
        // Coarse Y wraps around.  Set it to 0 and switch vertical nametable.
        coarseY = 0;
        v ^= 0b0000_1000_0000_0000;
      } else if (coarseY == 32) {
        // Coarse Y wraps around, BUT don't switch the vertical nametable.
        coarseY = 0;
      }
    }

    v &= 0b0000_1100_0001_1111;
    v |= (short) (fineY << 12);
    v |= (short) (coarseY << 5);
  }

  private void setVBlank0() {
    vblPending = true;
  }

  private void setVBlank1() {
    if (vblPending) {
      status = (byte) (status | 0b1000_0000);
    }
    vblPending = false;
  }

  private void setVBlank2() {
    nmiPending = isVblEnabled() && isNmiEnabled();
  }

  private void setVBlank3() {
    if (nmiPending) {
      nmi = true;
    }
    nmiPending = false;
  }

  private void clearVBlank() {
    status = (byte) (status & 0b0111_1111);
  }

  private void clearSprite0Hit() {
    status = (byte) (status & 0b1011_1111);
  }

  private void reloadVertical() {
    if (!isRenderingEnabled()) {
      return;
    }
    // vert(v) = vert(t)
    // v: GHIA.BC DEF..... <- t: GHIA.BC DEF.....
    v &= 0b0000_0100_0001_1111;
    v |= (short) (t & 0b0111_1011_1110_0000);
  }

  private void reloadHorizontal() {
    if (!isRenderingEnabled()) {
      return;
    }
    // horiz(v) = horiz(t)
    // v: ....A.. ...BCDEF <- t: ....A.. ...BCDEF
    v &= 0b0111_1011_1110_0000;
    v |= (short) (t & 0b0000_0100_0001_1111);
  }

  private void renderPixel() {
    if (!isRenderingEnabled()) {
      return;
    }
    int patternHi = patternHiByte.value(x);
    int patternLo = patternLoByte.value(x);
    int attribute = attributeByte.value();
    NesPpuColor[] colors = getBackgroundPalette(attribute);
    NesPpuColor color =
        switch ((patternHi << 1) | patternLo) {
          case 0 -> colors[0];
          case 1 -> colors[1];
          case 2 -> colors[2];
          case 3 -> colors[3];
          default -> throw new IllegalStateException();
        };
    int i = 3 * (scanline * 256 + dot - 1);
    frame[i + 0] = color.r();
    frame[i + 1] = color.g();
    frame[i + 2] = color.b();
  }

  private NesPpuColor[] getBackgroundPalette(int attribute) {
    return new NesPpuColor[] {
      palette.get(paletteIndexes[0]),
      palette.get(paletteIndexes[attribute * 4 + 1]),
      palette.get(paletteIndexes[attribute * 4 + 2]),
      palette.get(paletteIndexes[attribute * 4 + 3]),
    };
  }

  private void shiftRegisters() {
    nametableByte.shift();
    attributeByte.shift();
    patternLoByte.shift();
    patternHiByte.shift();
  }

  private void fetchNametableByte1() {}

  private void fetchNametableByte2() {
    if (!isRenderingEnabled()) {
      return;
    }
    nametableByte.set(ram[v & 0x0fff]);
  }

  private void fetchAttributeByte1() {}

  private void fetchAttributeByte2() {
    if (!isRenderingEnabled()) {
      return;
    }
    //                        0b0yyy_NNYY_YYYX_XXXX
    int nametab = (v >>> 0) & 0b0000_1100_0000_0000;
    int coarseY = (v >>> 4) & 0b0000_0000_0011_1000;
    int coarseX = (v >>> 2) & 0b0000_0000_0000_0111;
    int address = 0b0000_0011_1100_0000 | nametab | coarseY | coarseX;
    attributeByte.set(ram[address]);
  }

  private void fetchPatternByteLo1() {}

  private void fetchPatternByteLo2() {
    if (!isRenderingEnabled()) {
      return;
    }
    patternLoByte.set(fetchPatternTableByte(0));
  }

  private void fetchPatternByteHi1() {}

  private void fetchPatternByteHi2() {
    if (!isRenderingEnabled()) {
      return;
    }
    patternHiByte.set(fetchPatternTableByte(8));
  }

  private byte fetchPatternTableByte(int offset) {
    int chrBank = isAltBgPatternTable() ? 0b0001_0000_0000_0000 : 0b0000_0000_0000_0000;
    int nametable = Byte.toUnsignedInt(nametableByte.value()) << 4;
    int fineY = (v >> 12) & 0b0111;
    int chrIndex = chrBank | nametable | offset | fineY;
    return mapper.readChr((short) chrIndex);
  }

  private void reloadShiftRegisters() {
    if (!isRenderingEnabled()) {
      return;
    }
    nametableByte.reload();
    attributeByte.reload();
    patternLoByte.reload();
    patternHiByte.reload();
  }

  private void evaluateSprite() {
    if (!isRenderingEnabled()) {
      return;
    }
    oam.tick(scanline, dot);
  }

  private void fetchSprite0PatternByteLo1() {}

  private void fetchSprite0PatternByteLo2() {}

  private void fetchSprite0PatternByteHi1() {}

  private void fetchSprite0PatternByteHi2() {}

  private void fetchSprite1PatternByteLo1() {}

  private void fetchSprite1PatternByteLo2() {}

  private void fetchSprite1PatternByteHi1() {}

  private void fetchSprite1PatternByteHi2() {}

  private void fetchSprite2PatternByteLo1() {}

  private void fetchSprite2PatternByteLo2() {}

  private void fetchSprite2PatternByteHi1() {}

  private void fetchSprite2PatternByteHi2() {}

  private void fetchSprite3PatternByteLo1() {}

  private void fetchSprite3PatternByteLo2() {}

  private void fetchSprite3PatternByteHi1() {}

  private void fetchSprite3PatternByteHi2() {}

  private void fetchSprite4PatternByteLo1() {}

  private void fetchSprite4PatternByteLo2() {}

  private void fetchSprite4PatternByteHi1() {}

  private void fetchSprite4PatternByteHi2() {}

  private void fetchSprite5PatternByteLo1() {}

  private void fetchSprite5PatternByteLo2() {}

  private void fetchSprite5PatternByteHi1() {}

  private void fetchSprite5PatternByteHi2() {}

  private void fetchSprite6PatternByteLo1() {}

  private void fetchSprite6PatternByteLo2() {}

  private void fetchSprite6PatternByteHi1() {}

  private void fetchSprite6PatternByteHi2() {}

  private void fetchSprite7PatternByteLo1() {}

  private void fetchSprite7PatternByteLo2() {}

  private void fetchSprite7PatternByteHi1() {}

  private void fetchSprite7PatternByteHi2() {}

  private boolean isVramIncrementVertical() {
    return bit8(ctrl, 2) == 1;
  }

  private boolean isAltBgPatternTable() {
    return bit8(ctrl, 4) == 1;
  }

  private boolean isNmiEnabled() {
    return bit8(ctrl, 7) == 1;
  }

  private boolean isVblEnabled() {
    return bit8(status, 7) == 1;
  }

  private boolean isRenderingEnabled() {
    return isBgRenderingEnabled() || isFgRenderingEnabled();
  }

  private boolean isBgRenderingEnabled() {
    return bit8(mask, 3) == 1;
  }

  private boolean isFgRenderingEnabled() {
    return bit8(mask, 4) == 1;
  }

  private static int bit8(byte value, int i) {
    return (Byte.toUnsignedInt(value) >>> i) & 1;
  }
}
