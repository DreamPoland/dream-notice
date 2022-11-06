package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.bukkit.legacy.LegacyColorProcessor;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.plugin.Plugin;

public class BukkitNoticeProvider {

    @Getter private static BukkitNoticeProvider instance;

    @Getter private final Plugin plugin;
    @Getter private final MiniMessage miniMessage;
    @Getter private final AudienceProvider audienceProvider;

    public BukkitNoticeProvider(@NonNull Plugin plugin) {
        instance = this;

        this.plugin = plugin;
        this.miniMessage = MiniMessage.builder()
                .postProcessor(new LegacyColorProcessor())
                .build();
        this.audienceProvider = BukkitAudiences.create(this.plugin);
    }

    public static BukkitNoticeProvider create(@NonNull Plugin plugin) {
        return new BukkitNoticeProvider(plugin);
    }

}
