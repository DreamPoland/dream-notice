package cc.dreamcode.notice.minecraft.adventure.bukkit;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;

public class AdventureNoticeProvider {

    @Getter private static AdventureNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final BukkitAudiences bukkitAudiences;

    public AdventureNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public static AdventureNoticeProvider create(@NonNull Plugin plugin) {
        return new AdventureNoticeProvider(plugin);
    }

}
