repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))
    implementation(project(":minecraft-adventure"))

    // -- bunege api -- (base)
    compileOnly("net.md-5:bungeecord-api:1.20-R0.1-SNAPSHOT")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.5")

    // -- kyori-adventure --
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-platform-bungeecord:4.3.3")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")
}