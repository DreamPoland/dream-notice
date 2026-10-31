package cc.dreamcode.notice.bungee.adapter;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@RequiredArgsConstructor
public class BungeeAudienceAdapter implements Audience {

    private final CommandSender sender;

    @Override
    public void sendMessage(@NonNull Component message) {
        String legacy = LegacyComponentSerializer.legacySection().serialize(message);
        this.sender.sendMessage(TextComponent.fromLegacyText(legacy));
    }

    @Override
    public void sendActionBar(@NonNull Component message) {
        String legacy = LegacyComponentSerializer.legacySection().serialize(message);
        if (this.sender instanceof ProxiedPlayer player) {
            player.sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(legacy));
            return;
        }
        this.sender.sendMessage(TextComponent.fromLegacyText(legacy));
    }

    @Override
    public void showTitle(@NonNull Title title) {
        if (this.sender instanceof ProxiedPlayer player) {
            String titleText = LegacyComponentSerializer.legacySection().serialize(title.title());
            String subtitleText = LegacyComponentSerializer.legacySection().serialize(title.subtitle());

            Title.Times times = title.times();
            int fadeIn = times != null ? (int) (times.fadeIn().toMillis() / 50L) : 10;
            int stay = times != null ? (int) (times.stay().toMillis() / 50L) : 70;
            int fadeOut = times != null ? (int) (times.fadeOut().toMillis() / 50L) : 20;

            ProxyServer proxy = ProxyServer.getInstance();
            if (proxy != null) {
                net.md_5.bungee.api.Title bTitle = proxy.createTitle();
                if (bTitle != null) {
                    bTitle.title(TextComponent.fromLegacyText(titleText));
                    bTitle.subTitle(TextComponent.fromLegacyText(subtitleText));
                    bTitle.fadeIn(fadeIn);
                    bTitle.stay(stay);
                    bTitle.fadeOut(fadeOut);
                    bTitle.send(player);
                }
            }
        }
    }

    @Override
    public void clearTitle() {
        if (this.sender instanceof ProxiedPlayer player) {
            ProxyServer proxy = ProxyServer.getInstance();
            if (proxy != null) {
                net.md_5.bungee.api.Title bTitle = proxy.createTitle();
                if (bTitle != null) {
                    bTitle.clear();
                    bTitle.send(player);
                }
            }
        }
    }

    @Override
    public void resetTitle() {
        if (this.sender instanceof ProxiedPlayer player) {
            ProxyServer proxy = ProxyServer.getInstance();
            if (proxy != null) {
                net.md_5.bungee.api.Title bTitle = proxy.createTitle();
                if (bTitle != null) {
                    bTitle.reset();
                    bTitle.send(player);
                }
            }
        }
    }

    @Override
    public void sendPlayerListHeaderAndFooter(@NonNull Component header, @NonNull Component footer) {
        if (this.sender instanceof ProxiedPlayer player) {
            String headerText = LegacyComponentSerializer.legacySection().serialize(header);
            String footerText = LegacyComponentSerializer.legacySection().serialize(footer);
            player.setTabHeader(TextComponent.fromLegacyText(headerText), TextComponent.fromLegacyText(footerText));
        }
    }
}
