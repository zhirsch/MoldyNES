java_library(
    name = "io",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu:__pkg__",
        "//src/test/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
    ],
    deps = [
        "@maven//:com_google_guava_guava",
        "@maven//:io_github_libsdl4j_libsdl4j",
        "@maven//:net_java_dev_jna_jna",
    ],
)
