repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))
    implementation(project(":minecraft-adventure"))

    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.5")

    // -- kyori-adventure --
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-platform-bukkit:4.3.3")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")
}