package cc.dreamcode.notice.minecraft.adventure.bungee;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public class AdventureBungeeNoticeProvider {

    @Getter private static AdventureBungeeNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final BungeeAudiences bungeeAudiences;

    public AdventureBungeeNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.bungeeAudiences = BungeeAudiences.create(plugin);
    }

    public static AdventureBungeeNoticeProvider create(@NonNull Plugin plugin) {
        return new AdventureBungeeNoticeProvider(plugin);
    }

}
