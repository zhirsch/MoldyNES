load("@rules_jvm_external//:defs.bzl", "artifact")

java_library(
    name = "clock",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io:__pkg__",
    ],
    deps = [
        artifact("org.slf4j:slf4j-api"),
    ],
)
