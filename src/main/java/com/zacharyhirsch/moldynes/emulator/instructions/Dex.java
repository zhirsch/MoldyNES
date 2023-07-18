package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;

import java.nio.ByteBuffer;

public class Dex implements Instruction {

    @Override
    public String describe() {
        return "DEX";
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
        regs.x = (byte) (regs.x - 1);

        regs.sr.set(Registers.STATUS_REGISTER_N, regs.x < 0);
        regs.sr.set(Registers.STATUS_REGISTER_Z, regs.x == 0);
    }
}
