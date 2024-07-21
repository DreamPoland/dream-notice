package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.notice.minecraft.NoticeType;
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

public class BungeeNotice extends AdventureNotice<BungeeNotice> implements BungeeSender {
    public BungeeNotice(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static BungeeNotice of(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        return new BungeeNotice(noticeType, noticeText);
    }

    public static BungeeNotice chat(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.CHAT, noticeText);
    }

    public static BungeeNotice actionBar(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.ACTION_BAR, noticeText);
    }

    public static BungeeNotice title(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.TITLE, noticeText);
    }

    public static BungeeNotice subtitle(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.TITLE, noticeText);
    }

    public static BungeeNotice titleSubtitle(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }

    @Override
    public void send(@NonNull CommandSender target) {
        this.wrapAndSend(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer).wrapAndSend(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::wrapAndSend);

        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        targets.forEach(notice::wrapAndSend);
        notice.clearRender();
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers().forEach(notice::wrapAndSend);
        notice.clearRender();
    }

    @Override
    public void sendBroadcast() {
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.wrapAndSend(ProxyServer.getInstance().getConsole());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers().forEach(notice::wrapAndSend);
        notice.wrapAndSend(ProxyServer.getInstance().getConsole());
        notice.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::wrapAndSend);

        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(notice::wrapAndSend);

        notice.clearRender();
    }


    private void wrapAndSend(@NonNull CommandSender target) {
        final BungeeAudiences bungeeAudiences = BungeeNoticeProvider.getInstance().getBungeeAudiences();
        this.sendFormatted(target, bungeeAudiences.sender(target));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target) {

        if (!(sender instanceof ProxiedPlayer)) {
            this.toSplitComponents().forEach(target::sendMessage);
            return;
        }

        final NoticeType noticeType = (NoticeType) this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                this.toSplitComponents().forEach(target::sendMessage);
                break;
            }
            case ACTION_BAR: {
                target.sendActionBar(this.toJoiningComponent());
                break;
            }
            case TITLE: {
                final Component component = this.toJoiningComponent();
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
                final Component emptyComponent = this.toJoiningComponent();

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

                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + NoticeImpl.lineSeparator() + ") to separate two messages.");
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
            default: {
                this.clearRender();

                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
            }
        }
    }
}
