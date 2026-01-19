load("@rules_jvm_external//:defs.bzl", "artifact")

config_setting(
    name = "macos",
    constraint_values = [
        "@platforms//os:osx",
    ],
)

java_binary(
    name = "MoldyNES",
    srcs = ["MoldyNES.java"],
    jvm_flags =
        select({
            ":macos": ["-XstartOnFirstThread"],
            "//conditions:default": [],
        }) + [
            "--enable-native-access=ALL-UNNAMED",
        ],
    resources = [
        "//src/main/resources:Composite_wiki.pal",
        "//src/main/resources:logback.xml",
    ],
    visibility = ["//roms:__pkg__"],
    runtime_deps = [artifact("ch.qos.logback:logback-classic")],
    deps = [
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/bus",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/clock",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/io",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/mapper",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/ppu",
        "//src/main/java/com/zacharyhirsch/moldynes/emulator/rom",
        artifact("com.google.guava:guava"),
        artifact("org.slf4j:slf4j-api"),
    ],
)
