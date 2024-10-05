rootProject.name = "dream-notice"

include(":core")

// -- platforms --
include(":bukkit")
include(":bungee")

// -- serializers (okaeri-configs) --
include(":serializer:bukkit-serializer")
include(":serializer:bungee-serializer")