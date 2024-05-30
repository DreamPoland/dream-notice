package cc.dreamcode.notice.adventure;

import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeNoticeProvider {

    @Getter private static BungeeNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final BungeeAudiences bungeeAudiences;

    public BungeeNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.bungeeAudiences = BungeeAudiences.create(plugin);
    }

    public static BungeeNoticeProvider create(@NonNull Plugin plugin) {
        return new BungeeNoticeProvider(plugin);
    }

}
