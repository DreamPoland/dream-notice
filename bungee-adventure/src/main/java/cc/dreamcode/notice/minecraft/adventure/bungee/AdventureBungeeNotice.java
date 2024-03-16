package cc.dreamcode.notice.minecraft.adventure.bungee;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.adventure.AdventureLegacy;
import cc.dreamcode.notice.minecraft.adventure.AdventureNotice;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class AdventureBungeeNotice extends AdventureNotice<AdventureBungeeNotice> implements AdventureBungeeSender {
    public AdventureBungeeNotice(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static AdventureBungeeNotice of(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        return new AdventureBungeeNotice(noticeType, noticeText);
    }

    @Override
    public void send(@NonNull CommandSender target) {
        final BungeeAudiences bungeeAudiences = AdventureBungeeNoticeProvider.getInstance().getBungeeAudiences();
        this.sendFormatted(target, bungeeAudiences.sender(target));
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer).send(target);
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::send);
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        targets.forEach(target -> this.with(mapReplacer).send(target));
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::send);
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        ProxyServer.getInstance().getPlayers().forEach(target -> this.with(mapReplacer).send(target));
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::send);
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(target -> this.with(mapReplacer).send(target));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target) {

        if (!(sender instanceof ProxiedPlayer)) {
            target.sendMessage(this.toComponent());
            return;
        }

        final MinecraftNoticeType minecraftNoticeType = (MinecraftNoticeType) this.getNoticeType();
        switch (minecraftNoticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                target.sendMessage(this.toComponent());
                break;
            }
            case ACTION_BAR: {
                target.sendActionBar(this.toComponent());
                break;
            }
            case TITLE: {
                final Component component = this.toComponent();
                final Component emptyComponent = AdventureLegacy.deserialize(" ");

                Title titleBuilder = Title.title(
                        component,
                        emptyComponent,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);
                break;
            }
            case SUBTITLE: {
                final Component component = AdventureLegacy.deserialize(" ");
                final Component emptyComponent = this.toComponent();

                Title titleBuilder = Title.title(
                        component,
                        emptyComponent,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);
                break;
            }
            case TITLE_SUBTITLE: {

                String[] split = this.getRender().split(MinecraftNotice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + MinecraftNotice.lineSeparator() + ") to separate two messages.");
                }

                final Component title = AdventureLegacy.deserialize(split[0]);
                final Component subTitle = AdventureLegacy.deserialize(split[1]);

                Title titleBuilder = Title.title(
                        title,
                        subTitle,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);
                break;
            }
            default:
                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
        }
    }
}
