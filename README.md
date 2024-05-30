
# Dream-Notice

Simple notice library with placeholders and mini-messages support.


## Platforms

- Bukkit/Spigot/Paper (need relocation) - [bukkit](https://github.com/DreamPoland/dream-notice/tree/master/bukkit) or [bukkit-adventure](https://github.com/DreamPoland/dream-notice/tree/master/bukkit)
- Paper (native-support 1.18.2+) - [paper-adventure](https://github.com/DreamPoland/dream-notice/tree/master/paper-adventure)
- Bungee/Waterfall (need relocation) - [bungee](https://github.com/DreamPoland/dream-notice/tree/master/bukkit) or [bungee-adventure](https://github.com/DreamPoland/dream-notice/tree/master/bukkit)

### Warning
Bukkit-Adventure and Bungee-Adventure require these methods: (on-enable)

`BukkitNoticeProvider.create(this)` or `BungeeNoticeProvider.create(this)`

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
  <version>1.5.0</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.notice:{platform}:1.5.0"
```

## Example

```java
BukkitNotice.of(NoticeType.CHAT, "&7Simple test {argument}.")
        .with("argument", "player1")
        .hoverEvent(HoverEvent.showText(AdventureLegacy.deserialize("Text.")))
        .clickEvent(ClickEvent.openUrl("https://dreamcode.cc"))
        .send(player);
```

