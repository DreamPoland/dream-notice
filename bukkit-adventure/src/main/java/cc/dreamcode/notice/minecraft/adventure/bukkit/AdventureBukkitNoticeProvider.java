package cc.dreamcode.notice.minecraft.adventure.bukkit;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;

public class AdventureBukkitNoticeProvider {

    @Getter private static AdventureBukkitNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final BukkitAudiences bukkitAudiences;

    public AdventureBukkitNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public static AdventureBukkitNoticeProvider create(@NonNull Plugin plugin) {
        return new AdventureBukkitNoticeProvider(plugin);
    }

}
