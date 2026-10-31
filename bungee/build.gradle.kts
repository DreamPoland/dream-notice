dependencies {
    api(project(":adventure"))

    api(libs.dream.utilities.bungee)
    compileOnly(libs.bungeecord.api)
    testImplementation(libs.bungeecord.api)
}
