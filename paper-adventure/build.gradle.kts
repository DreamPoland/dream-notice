repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))
    implementation(project(":minecraft-adventure"))

    // -- paper api -- (core)
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // -- kyori-adventure --
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.3")
    implementation("cc.dreamcode:utilities-bukkit:1.3.3")
}