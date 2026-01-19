load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "ppu",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        artifact("com.google.guava:guava"),
    ],
)
