java_library(
    name = "apu",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/clock",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
