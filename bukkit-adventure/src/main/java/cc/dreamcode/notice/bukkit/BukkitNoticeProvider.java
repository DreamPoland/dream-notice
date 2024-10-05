package cc.dreamcode.notice.bukkit;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;

public class BukkitNoticeProvider {

    @Getter private static BukkitNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final BukkitAudiences bukkitAudiences;

    public BukkitNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public static BukkitNoticeProvider create(@NonNull Plugin plugin) {
        return new BukkitNoticeProvider(plugin);
    }

}