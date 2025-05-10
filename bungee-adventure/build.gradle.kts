repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://repo.codemc.io/repository/maven-public")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- bungee api -- (base)
    compileOnly(libs.bungeecord.api)

    // -- dream-utilities --
    api(libs.dream.utilties.bungee.adventure)

    // -- placeholders --
    api(libs.okaeri.placeholders)

    // -- kyori-adventure --
    api(libs.adventure.minimessage)
    api(libs.adventure.serializer)
    api(libs.adventure.platform.bungee)
}