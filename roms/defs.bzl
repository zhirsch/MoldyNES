def define_roms(**kwargs):
    for rom in native.glob(["**/*.nes"]):
        name = rom.replace("/", "_")
        if name.endswith(".nes"):
            name = name[:-4]

        native.sh_binary(
            name = name,
            srcs = ["run_rom.sh"],
            args = ["roms/" + rom],
            data = [
                rom,
                "//src/main/java/com/zacharyhirsch/moldynes/emulator:MoldyNES",
            ],
            **kwargs
        )
