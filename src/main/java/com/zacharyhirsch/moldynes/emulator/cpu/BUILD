load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "cpu",
    srcs = glob(["**/*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        artifact("org.slf4j:slf4j-api"),
    ],
)
