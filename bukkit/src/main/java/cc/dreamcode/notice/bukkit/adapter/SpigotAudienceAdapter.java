package cc.dreamcode.notice.bukkit.adapter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.inventory.Book;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
public class SpigotAudienceAdapter implements Audience {

    private final CommandSender sender;
    private static final Map<UUID, org.bukkit.boss.BossBar> ACTIVE_BOSS_BARS = new ConcurrentHashMap<>();

    @Override
    public void sendMessage(@NonNull Component message) {
        String legacy = LegacyComponentSerializer.legacySection().serialize(message);
        this.sender.sendMessage(legacy);
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        String legacy = LegacyComponentSerializer.legacySection().serialize(message);
        if (this.sender instanceof Player player) {
            try {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(legacy));
                return;
            } catch (Throwable ignored) {
            }
        }
        this.sender.sendMessage(legacy);
    }

    @Override
    public void showTitle(@NonNull Title title) {
        if (this.sender instanceof Player player) {
            String titleText = LegacyComponentSerializer.legacySection().serialize(title.title());
            String subtitleText = LegacyComponentSerializer.legacySection().serialize(title.subtitle());

            Title.Times times = title.times();
            int fadeIn = times != null ? (int) (times.fadeIn().toMillis() / 50L) : 10;
            int stay = times != null ? (int) (times.stay().toMillis() / 50L) : 70;
            int fadeOut = times != null ? (int) (times.fadeOut().toMillis() / 50L) : 20;

            try {
                player.sendTitle(titleText, subtitleText, fadeIn, stay, fadeOut);
            } catch (Throwable t) {
                try {
                    player.sendTitle(titleText, subtitleText);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public void clearTitle() {
        if (this.sender instanceof Player player) {
            try {
                player.sendTitle("", "", 0, 0, 0);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void resetTitle() {
        if (this.sender instanceof Player player) {
            try {
                player.resetTitle();
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        if (this.sender instanceof Player player) {
            String headerText = LegacyComponentSerializer.legacySection().serialize(header);
            String footerText = LegacyComponentSerializer.legacySection().serialize(footer);
            try {
                player.setPlayerListHeaderFooter(headerText, footerText);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void playSound(@NonNull Sound sound) {
        if (this.sender instanceof Player player) {
            String soundName = sound.name().value();
            try {
                org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(soundName.toUpperCase().replace('.', '_'));
                player.playSound(player.getLocation(), bukkitSound, sound.volume(), sound.pitch());
            } catch (Throwable t) {
                try {
                    player.playSound(player.getLocation(), sound.name().asString(), sound.volume(), sound.pitch());
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public void playSound(@NonNull Sound sound, double x, double y, double z) {
        if (this.sender instanceof Player player) {
            Location loc = new Location(player.getWorld(), x, y, z);
            String soundName = sound.name().value();
            try {
                org.bukkit.Sound bukkitSound = org.bukkit.Sound.valueOf(soundName.toUpperCase().replace('.', '_'));
                player.playSound(loc, bukkitSound, sound.volume(), sound.pitch());
            } catch (Throwable t) {
                try {
                    player.playSound(loc, sound.name().asString(), sound.volume(), sound.pitch());
                } catch (Throwable ignored) {
                }
            }
        }
    }

    @Override
    public void openBook(@NonNull Book book) {
        if (this.sender instanceof Player player) {
            try {
                ItemStack item = new ItemStack(Material.WRITTEN_BOOK);
                BookMeta meta = (BookMeta) item.getItemMeta();
                if (meta != null) {
                    meta.setTitle(LegacyComponentSerializer.legacySection().serialize(book.title()));
                    meta.setAuthor(LegacyComponentSerializer.legacySection().serialize(book.author()));
                    for (Component page : book.pages()) {
                        meta.addPage(LegacyComponentSerializer.legacySection().serialize(page));
                    }
                    item.setItemMeta(meta);
                }
                player.openBook(item);
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void showBossBar(@NonNull BossBar bar) {
        if (this.sender instanceof Player player) {
            try {
                String title = LegacyComponentSerializer.legacySection().serialize(bar.name());
                BarColor color;
                try {
                    color = BarColor.valueOf(bar.color().name().toUpperCase());
                } catch (Throwable t) {
                    color = BarColor.WHITE;
                }

                BarStyle style = BarStyle.SOLID;
                try {
                    style = BarStyle.valueOf(bar.overlay().name().toUpperCase());
                } catch (Throwable ignored) {
                }

                org.bukkit.boss.BossBar bukkitBar = Bukkit.createBossBar(title, color, style);
                bukkitBar.setProgress(Math.max(0.0, Math.min(1.0, bar.progress())));
                bukkitBar.addPlayer(player);
                bukkitBar.show();

                org.bukkit.boss.BossBar previous = ACTIVE_BOSS_BARS.put(player.getUniqueId(), bukkitBar);
                if (previous != null) {
                    previous.removeAll();
                }
            } catch (Throwable ignored) {
            }
        }
    }

    @Override
    public void hideBossBar(@NonNull BossBar bar) {
        if (this.sender instanceof Player player) {
            org.bukkit.boss.BossBar bukkitBar = ACTIVE_BOSS_BARS.remove(player.getUniqueId());
            if (bukkitBar != null) {
                bukkitBar.removePlayer(player);
            }
        }
    }
}
