package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.bungee.legacy.LegacyColorProcessor;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.md_5.bungee.api.plugin.Plugin;

public class BungeeNoticeProvider {

    @Getter private static BungeeNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final MiniMessage miniMessage;
    @Getter private final AudienceProvider audienceProvider;

    public BungeeNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();
        this.audienceProvider = BungeeAudiences.create(this.plugin);
    }

    public static BungeeNoticeProvider create(@NonNull Plugin plugin) {
        return new BungeeNoticeProvider(plugin);
    }

}
