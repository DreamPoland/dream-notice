
# Dream-Notice

Simple notice library with placeholders and minimessages.


## Platforms

- Bukkit/Spigot/Paper - [bukkit](https://github.com/DreamPoland/dream-notice/tree/master/bukkit) or [bukkit-adventure](https://github.com/DreamPoland/dream-notice/tree/master/bukkit)
- Paper (native-support 1.18.2+) - [paper-adventure](https://github.com/DreamPoland/dream-notice/tree/master/paper-adventure)
- Bungee/Waterfall - [bungee](https://github.com/DreamPoland/dream-notice/tree/master/bukkit) or [bungee-adventure](https://github.com/DreamPoland/dream-notice/tree/master/bukkit)
## Maven/Gradle

### Maven
```xml
<repository>
  <id>dreamcode-repository-releases</id>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```

```xml
<dependency>
  <groupId>cc.dreamcode.notice</groupId>
  <artifactId>{platform}</artifactId>
  <version>1.4-beta.11</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.notice:{platform}:1.4-beta.11"
```

## Example

```java
AdventureBukkitNotice.of(MinecraftNoticeType.CHAT, "&7Simple test {argument}.")
        .with("argument", "player1")
        .hoverEvent(HoverEvent.showText(AdventureLegacy.deserialize("Text.")))
        .clickEvent(ClickEvent.openUrl("https://dreamcode.cc"))
        .send(player);
```

