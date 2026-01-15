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
        "@maven//:com_google_guava_guava",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
