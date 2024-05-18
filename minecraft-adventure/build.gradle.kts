dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))

    // -- kyori-adventure --
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.1")
    implementation("cc.dreamcode:utilities-bungee:1.4.1")

    // -- okaeri-placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")
}