package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
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

public class BungeeNotice extends Notice<BungeeNotice> implements BungeeSender {
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
        this.with(mapReplacer);
        this.sendFormatted(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendBroadcast() {
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.sendFormatted(ProxyServer.getInstance().getConsole());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        ProxyServer.getInstance().getPlayers().forEach(this::sendFormatted);
        this.sendFormatted(ProxyServer.getInstance().getConsole());
        this.clearRender();
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
        this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::sendFormatted);

        this.clearRender();
    }

    private void sendFormatted(@NonNull CommandSender target) {
        if (!(target instanceof ProxiedPlayer)) {
            Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text ->
                    target.sendMessage(new TextComponent(this.placeholdersExists()
                            ? StringColorUtil.fixColor(text, this.getPlaceholders())
                            : StringColorUtil.fixColor(text))));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) target;
        final NoticeType noticeType = this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text ->
                        target.sendMessage(new TextComponent(this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text))));
                break;
            }
            case ACTION_BAR: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");
                player.sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text)));
                break;
            }
            case TITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.title(new TextComponent(this.placeholdersExists()
                        ? StringColorUtil.fixColor(text, this.getPlaceholders())
                        : StringColorUtil.fixColor(text)));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);
                break;
            }
            case SUBTITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.subTitle(new TextComponent(this.placeholdersExists()
                        ? StringColorUtil.fixColor(text, this.getPlaceholders())
                        : StringColorUtil.fixColor(text)));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = this.getNoticeText().split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + Notice.lineSeparator() + ") to separate two messages.");
                }

                final String title;
                final String subTitle;

                if (this.placeholdersExists()) {
                    title = StringColorUtil.fixColor(split[0], this.getPlaceholders());
                    subTitle = StringColorUtil.fixColor(split[1], this.getPlaceholders());
                }
                else {
                    title = StringColorUtil.fixColor(split[0]);
                    subTitle = StringColorUtil.fixColor(split[1]);
                }

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
