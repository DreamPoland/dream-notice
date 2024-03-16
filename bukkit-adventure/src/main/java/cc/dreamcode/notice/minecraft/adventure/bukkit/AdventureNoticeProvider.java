package cc.dreamcode.notice.minecraft.adventure.bukkit;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.Plugin;

public class AdventureNoticeProvider {

    @Getter private static AdventureNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final AudienceProvider audienceProvider;

    public AdventureNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.audienceProvider = BukkitAudiences.create(this.plugin);
    }

    public static AdventureNoticeProvider create(@NonNull Plugin plugin) {
        return new AdventureNoticeProvider(plugin);
    }

}
