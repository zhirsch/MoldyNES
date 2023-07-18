package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Registers;

import java.nio.ByteBuffer;

public class Dey implements Instruction {

    @Override
    public String describe() {
        return "DEY";
    }

    @Override
    public void execute(ByteBuffer ram, Registers regs) {
        regs.y = (byte) (regs.y - 1);

        regs.sr.set(Registers.STATUS_REGISTER_N, regs.y < 0);
        regs.sr.set(Registers.STATUS_REGISTER_Z, regs.y == 0);
    }
}
