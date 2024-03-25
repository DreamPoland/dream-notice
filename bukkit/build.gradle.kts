repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":minecraft"))

    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.3.0")
    implementation("cc.dreamcode:utilities-bukkit:1.3.0")

    // -- x-series --
    implementation("com.github.cryptomorin:XSeries:9.9.0")
}