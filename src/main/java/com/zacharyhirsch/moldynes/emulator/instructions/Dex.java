package com.zacharyhirsch.moldynes.emulator.instructions;

import com.zacharyhirsch.moldynes.emulator.Ram;
import com.zacharyhirsch.moldynes.emulator.Registers;
import com.zacharyhirsch.moldynes.emulator.Stack;

public class Dex implements Instruction {

    @Override
    public String describe() {
        return "DEX";
    }

    @Override
    public void execute(Ram ram, Registers regs, Stack stack) {
        regs.x = (byte) (regs.x - 1);

        regs.sr.n = regs.x < 0;
        regs.sr.z = regs.x == 0;
    }
}
