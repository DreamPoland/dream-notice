dependencies {
    api(project(":core"))
    api(project(":bungee-adventure"))

    // -- okaeri-configs --
    implementation(libs.okaeri.configs)

    // -- kyori-adventure --
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.serializer)
}