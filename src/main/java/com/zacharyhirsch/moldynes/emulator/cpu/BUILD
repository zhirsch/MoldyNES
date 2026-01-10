java_library(
    name = "cpu",
    srcs = glob(["**/*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
