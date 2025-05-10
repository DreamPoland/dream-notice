# Dream-Notice
[![Build](https://github.com/DreamPoland/dream-notice/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-notice/actions/workflows/gradle.yml)

Simple notice library with placeholders and mini-messages support.

## Platforms

- Bukkit/Spigot - [bukkit](https://github.com/DreamPoland/dream-notice/tree/master/bukkit)
- Paper - [paper](https://github.com/DreamPoland/dream-notice/tree/master/paper)
- Bungee/Waterfall - [bungee](https://github.com/DreamPoland/dream-notice/tree/master/bungee)

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
  <version>1.6.8</version>
</dependency>
```

### Gradle
```groovy
maven { url "https://repo.dreamcode.cc/releases" }
```

```groovy
implementation "cc.dreamcode.notice:{platform}:1.6.8"
```

## Note

If you need mini-message support, you need to implement dream-utilities versions with the adventure module. Both dream-utilities modules run on the same package.

For example `cc.dreamcode:utilities-bukkit-adventure:newer-version`

Or use module with -adventure suffix, `cc.dreamcode.notice:platform-adventure:1.6.8` for better support. (use only core and adventure module)

Paper module works on bukkit-adventure files without kyori-adventure implementation