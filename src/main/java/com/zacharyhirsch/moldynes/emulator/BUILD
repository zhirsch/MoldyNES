java_library(
    name = "emulator",
    srcs = glob(["*.java"]),
    visibility = ["//src/test/java/com/zacharyhirsch/moldynes/emulator:__pkg__"],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/apu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mappers",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "@maven//:com_google_guava_guava",
    ],
)

java_binary(
    name = "MoldyNES",
    srcs = ["MoldyNES.java"],
    jvm_flags = [
        "-XstartOnFirstThread",
        "--enable-native-access=ALL-UNNAMED",
    ],
    resources = ["//src/main/resources:Composite_wiki.pal"],
    visibility = ["//roms:__pkg__"],
    deps = [
        ":emulator",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mappers",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "@maven//:com_google_guava_guava",
    ],
)
