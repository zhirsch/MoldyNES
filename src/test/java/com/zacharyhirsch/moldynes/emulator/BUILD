load("@contrib_rules_jvm//java:defs.bzl", "java_junit5_test", "junit5_deps")

java_junit5_test(
    name = "InstrTestV5Tests",
    srcs = ["InstrTestV5Tests.java"],
    data = [
        "//roms:instr_test-v5/rom_singles/01-basics.nes",
        "//roms:instr_test-v5/rom_singles/02-implied.nes",
        "//roms:instr_test-v5/rom_singles/03-immediate.nes",
        "//roms:instr_test-v5/rom_singles/04-zero_page.nes",
        "//roms:instr_test-v5/rom_singles/05-zp_xy.nes",
        "//roms:instr_test-v5/rom_singles/06-absolute.nes",
        "//roms:instr_test-v5/rom_singles/07-abs_xy.nes",
        "//roms:instr_test-v5/rom_singles/08-ind_x.nes",
        "//roms:instr_test-v5/rom_singles/09-ind_y.nes",
        "//roms:instr_test-v5/rom_singles/10-branches.nes",
        "//roms:instr_test-v5/rom_singles/11-stack.nes",
        "//roms:instr_test-v5/rom_singles/12-jmp_jsr.nes",
        "//roms:instr_test-v5/rom_singles/13-rts.nes",
        "//roms:instr_test-v5/rom_singles/14-rti.nes",
        "//roms:instr_test-v5/rom_singles/15-brk.nes",
        "//roms:instr_test-v5/rom_singles/16-special.nes",
    ],
    jvm_flags = [
        "-Djava.security.manager=disallow",
    ],
    resources = ["//src/main/resources:Composite_wiki.pal"],
    runtime_deps = junit5_deps(),
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mappers",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "@maven//:com_google_truth_truth",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)
