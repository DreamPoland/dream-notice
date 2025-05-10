repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- spigot api -- (base)
    compileOnly(libs.paper.api)

    // -- dream-utilities --
    api(libs.dream.utilties.bukkit.adventure)

    // -- kyori-adventure --
    compileOnly(libs.adventure.minimessage)
    compileOnly(libs.adventure.serializer)
}