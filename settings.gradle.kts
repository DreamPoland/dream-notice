pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "dream-notice"

include(":core")

// -- platforms --
include(":bukkit")
include(":bukkit-adventure")
include(":paper")
include(":bungee")
include(":bungee-adventure")

// -- serializers (okaeri-configs) --
include(":serializer:bukkit-serializer")
include(":serializer:bungee-serializer")
include(":serializer:paper-serializer")
include(":serializer:bukkit-adventure-serializer")
include(":serializer:bungee-adventure-serializer")