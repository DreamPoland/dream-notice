dependencies {
    api(project(":notice-adventure"))

    api(libs.dream.utilities.bukkit)
    compileOnly(libs.paper.api)
    testImplementation(libs.paper.api)
}
