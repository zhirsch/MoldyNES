load("@rules_java//java:java_library.bzl", "java_library")

java_library(
    name = "rom",
    srcs = glob(["*.java"]),
    visibility = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator:__pkg__",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper:__pkg__",
    ],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/memory",
    ],
)
