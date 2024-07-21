package cc.dreamcode.notice.minecraft;

import cc.dreamcode.utilities.bungee.StringColorUtil;
import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BungeeNotice extends NoticeImpl<BungeeNotice> implements BungeeSender {
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
        this.sendFormatted(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer).sendFormatted(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        targets.forEach(notice::sendFormatted);
        notice.clearRender();
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers().forEach(notice::sendFormatted);
        notice.clearRender();
    }

    @Override
    public void sendBroadcast() {
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.sendFormatted(ProxyServer.getInstance().getConsole());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers().forEach(notice::sendFormatted);
        notice.sendFormatted(ProxyServer.getInstance().getConsole());
        notice.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::sendFormatted);

        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        final BungeeNotice notice = this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(notice::sendFormatted);

        notice.clearRender();
    }

    private void sendFormatted(@NonNull CommandSender target) {
        if (!(target instanceof ProxiedPlayer)) {
            String[] split = this.getRender().split(NoticeImpl.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    target.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) target;
        final NoticeType noticeType = (NoticeType) this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));
                break;
            }
            case ACTION_BAR: {
                player.sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));
                break;
            }
            case TITLE: {
                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.title(new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);
                break;
            }
            case SUBTITLE: {
                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.subTitle(new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + NoticeImpl.lineSeparator() + ") to separate two messages.");
                }

                final String title = StringColorUtil.fixColor(split[0]);
                final String subTitle = StringColorUtil.fixColor(split[1]);

                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.title(new TextComponent(title));
                titleBuilder.subTitle(new TextComponent(subTitle));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);
                break;
            }
            default: {
                this.clearRender();

                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
            }
        }
    }
}
