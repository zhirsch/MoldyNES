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
        "@maven//:com_google_guava_guava",
        "@maven//:net_java_dev_jna_jna",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
