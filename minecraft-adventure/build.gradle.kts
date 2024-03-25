dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))

    // -- kyori-adventure --
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.16.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
    implementation("cc.dreamcode:utilities-bungee:1.3.0")

    // -- okaeri-placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")
}