load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "io",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/clock",
        "//third_party/sdl3",
        artifact("com.google.guava:guava"),
        artifact("net.java.dev.jna:jna"),
        artifact("org.slf4j.slf4j:api"),
    ],
)
