rootProject.name = "dream-notice"

include(":core")
include(":minecraft")

// -- platforms --
include(":bukkit")
include(":bungee")

// -- serializers (okaeri-configs) --
include(":serializer:bukkit-serializer")
include(":serializer:bungee-serializer")