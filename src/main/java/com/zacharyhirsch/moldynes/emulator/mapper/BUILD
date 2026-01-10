java_library(
    name = "mapper",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/cpu:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/rom",
        "@maven//:org_slf4j_slf4j_api",
    ],
)
