package cc.dreamcode.notice.minecraft.adventure.bungee;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public class AdventureNoticeProvider {

    @Getter private static AdventureNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final AudienceProvider audienceProvider;

    public AdventureNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.audienceProvider = BungeeAudiences.create(this.plugin);
    }

    public static AdventureNoticeProvider create(@NonNull Plugin plugin) {
        return new AdventureNoticeProvider(plugin);
    }

}
