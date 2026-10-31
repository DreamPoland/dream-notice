# Dream-Notice

[![Build](https://github.com/DreamPoland/dream-notice/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-notice/actions/workflows/gradle.yml)
[![Version](https://img.shields.io/badge/version-2.0.0-blue.svg)](https://repo.dreamcode.cc/#/releases/cc/dreamcode/notice)
[![Java](https://img.shields.io/badge/java-21%2B-orange.svg)](https://www.oracle.com/java/)

Modern, modular Minecraft notification and messaging library powered by **Kyori Adventure**, with **MiniMessage**, placeholders, and **Okaeri Configs** support.

---

## Features

- **Kyori Adventure & MiniMessage**: Full rich text formatting, RGB colors, gradients, and legacy color codes.
- **Multi-Platform Support**:
  - **Paper / Purpur**: Native Adventure audiences out-of-the-box.
  - **Spigot / Bukkit**: Built-in `SpigotAudienceAdapter` for seamless Spigot compatibility.
  - **BungeeCord / Waterfall**: Built-in `BungeeAudienceAdapter` for native Bungee proxies.
- **Multiple Notice Types**: Chat, ActionBar, Title/Subtitle, BossBar, Books, PlayerList (Tablist), and Sounds.
- **Okaeri Configs Integration**: Serialization and deserialization pack (`NoticeSerdes`) for single-line and complex notices.
- **Fluent Builder API**: Type-safe notice sending with placeholders, sync/async scheduling, and broadcasting.

---

## Requirements

- **Java 21** or newer.
- **Paper / Spigot / Bukkit** 1.20+ or **BungeeCord** 1.20+.

---

## Installation

### Maven

Add the repository:
```xml
<repositories>
  <repository>
    <id>dreamcode-repository-releases</id>
    <url>https://repo.dreamcode.cc/releases</url>
  </repository>
</repositories>
```

Add the dependency for your platform:

**Bukkit / Spigot / Paper:**
```xml
<dependency>
  <groupId>cc.dreamcode.notice</groupId>
  <artifactId>bukkit</artifactId>
  <version>2.0.2</version>
</dependency>
```

**BungeeCord:**
```xml
<dependency>
  <groupId>cc.dreamcode.notice</groupId>
  <artifactId>bungee</artifactId>
  <version>2.0.2</version>
</dependency>
```

**Bukkit Serializer (Okaeri Configs):**
```xml
<dependency>
  <groupId>cc.dreamcode.notice</groupId>
  <artifactId>bukkit-serializer</artifactId>
  <version>2.0.2</version>
</dependency>
```

**BungeeCord Serializer (Okaeri Configs):**
```xml
<dependency>
  <groupId>cc.dreamcode.notice</groupId>
  <artifactId>bungee-serializer</artifactId>
  <version>2.0.2</version>
</dependency>
```

---

### Gradle (Kotlin DSL)

```kotlin
repositories {
    maven("https://repo.dreamcode.cc/releases")
}

dependencies {
    // Bukkit / Spigot / Paper
    implementation("cc.dreamcode.notice:bukkit:2.0.2")
    implementation("cc.dreamcode.notice:bukkit-serializer:2.0.2")

    // BungeeCord
    implementation("cc.dreamcode.notice:bungee:2.0.2")
    implementation("cc.dreamcode.notice:bungee-serializer:2.0.2")
}
```

---

## Usage

### 1. Defining Notices

You can create platform-specific notices directly using static factory methods:

**Bukkit / Paper / Spigot:**
```java
public BukkitNotice notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
public BukkitNotice welcome = BukkitNotice.chat("Welcome <gradient:#ff0000:#00ff00>{player}</gradient> to the server!");
public BukkitNotice actionbar = BukkitNotice.actionbar("<gold>+10 coins!</gold>");
public BukkitNotice title = BukkitNotice.title("<green>LEVEL UP!</green>", "<gray>You reached level {level}</gray>", 10, 70, 20);
public BukkitNotice bossBar = BukkitNotice.bossBar("<red>Boss incoming!</red>", "RED", "PROGRESS", 0.75f);
public BukkitNotice sound = BukkitNotice.sound("entity.player.levelup", "MASTER", 1.0f, 1.0f);
```

**BungeeCord:**
```java
public BungeeNotice notPlayer = BungeeNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
public BungeeNotice welcome = BungeeNotice.chat("Welcome {player} to proxy!");
```

---

### 2. Integration with Okaeri Configs

Each module has its dedicated Okaeri Configs serializer (`BukkitNoticeSerdes`, `BungeeNoticeSerdes`, `AdventureNoticeSerdes`, `NoticeSerdes`).

```java
public class PluginConfig extends OkaeriConfig {

    public BukkitNotice notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
    public BukkitNotice welcomeMessage = BukkitNotice.chat("Welcome <green>{player}</green>!");
    public BukkitNotice levelUpTitle = BukkitNotice.title("<gold>Level Up!</gold>", "<yellow>Level {level}</yellow>");
    public BukkitNotice actionbarNotice = BukkitNotice.actionbar("<yellow>Saved progress.</yellow>");

}
```

Register `BukkitNoticeSerdes` when creating the config:

```java
PluginConfig config = ConfigManager.create(PluginConfig.class, it -> {
    it.withConfigurer(new YamlBukkitConfigurer(), new BukkitNoticeSerdes());
    it.withBindFile(new File(getDataFolder(), "config.yml"));
    it.saveDefaults();
    it.load(true);
});
```

---

### 3. Initializing the Provider

#### On Bukkit / Spigot / Paper:
```java
// With config:
BukkitNoticeProvider<PluginConfig> provider = BukkitNoticeProvider.create(config, plugin);

// Without config:
BukkitNoticeProvider<Object> provider = BukkitNoticeProvider.create(plugin);
```

#### On BungeeCord:
```java
// With config:
BungeeNoticeProvider<PluginConfig> provider = BungeeNoticeProvider.create(config, plugin);

// Without config:
BungeeNoticeProvider<Object> provider = BungeeNoticeProvider.create(plugin);
```

---

### 4. Sending Notices

#### Via Fluent Builder (Recommended):

```java
// Send to single player
provider.builder()
    .player(player)
    .notice(c -> c.welcomeMessage)
    .with("player", player.getName())
    .send();

// Broadcast to everyone
provider.builder()
    .all()
    .notice(c -> c.welcomeMessage)
    .with("player", "Everyone")
    .send();

// Send asynchronously
provider.builder()
    .player(player)
    .notice(c -> c.actionbarNotice)
    .async()
    .send();
```

#### Direct Model Send:

```java
Notice.chat("Hello {player}!")
    .send(player, "player", player.getName());
```

---

## License

This project is licensed under the [MIT License](LICENSE).
