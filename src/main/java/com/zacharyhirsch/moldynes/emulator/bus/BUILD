load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "bus",
    srcs = glob(["*.java"]),
    visibility = ["//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__"],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/clock",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        artifact("com.google.guava:guava"),
        artifact("org.slf4j:slf4j-api"),
    ],
)
