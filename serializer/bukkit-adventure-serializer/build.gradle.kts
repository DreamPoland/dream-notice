dependencies {
    api(project(":core"))
    api(project(":bukkit-adventure"))

    // -- okaeri-configs --
    api(libs.okaeri.configs)

    // -- kyori-adventure --
    api(libs.adventure.minimessage)
    api(libs.adventure.serializer)
}