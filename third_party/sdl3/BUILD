config_setting(
    name = "macos",
    constraint_values = [
        "@platforms//os:osx",
    ],
)

config_setting(
    name = "windows",
    constraint_values = [
        "@platforms//os:windows",
    ],
)

java_import(
    name = "sdl3",
    jars = select({
        ":macos": ["macos-arm64_x86_64/SDL3.jar"],
        ":windows": ["win32-x64/SDL3.jar"],
    }),
    srcjar = select({
        ":macos": "macos-arm64_x86_64/SDL3-sources.jar",
        ":windows": "win32-x64/SDL3-sources.jar",
    }),
    visibility = ["//src/main/java/com/zacharyhirsch/moldynes/emulator/io:__pkg__"],
)
