repositories {
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))

    // -- bungeecord-chat --
    compileOnly("net.md-5:bungeecord-chat:1.20-R0.1-SNAPSHOT")

    // -- kyori-adventure --
    compileOnly("net.kyori:adventure-text-minimessage:4.17.0")
    compileOnly("net.kyori:adventure-text-serializer-legacy:4.17.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.2")

    // -- okaeri-placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")
}