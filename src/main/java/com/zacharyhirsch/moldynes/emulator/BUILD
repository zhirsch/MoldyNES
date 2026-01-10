java_library(
    name = "emulator",
    srcs = glob(
        ["*.java"],
        exclude = ["MoldyNES.java"],
    ),
    resources = ["//src/main/resources:logback.xml"],
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

java_binary(
    name = "MoldyNES",
    srcs = ["MoldyNES.java"],
    jvm_flags = [
        "-XstartOnFirstThread",
        "--enable-native-access=ALL-UNNAMED",
    ],
    resources = ["//src/main/resources:Composite_wiki.pal"],
    visibility = ["//roms:__pkg__"],
    runtime_deps = ["@maven//:ch_qos_logback_logback_classic"],
    deps = [
        ":emulator",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/clock",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/rom",
        "@maven//:com_google_guava_guava",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
