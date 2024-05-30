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

    @Override
    public void send(@NonNull CommandSender target) {
        this.sendFormatted(target);
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

    private void sendFormatted(@NonNull CommandSender target) {
        if (!(target instanceof ProxiedPlayer)) {
            String[] split = this.getRender().split(NoticeImpl.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    target.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));

            this.clearRender();
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) target;
        final NoticeType noticeType = (NoticeType) this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                this.clearRender();
                break;
            }
            case CHAT: {
                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));

                this.clearRender();
                break;
            }
            case ACTION_BAR: {
                player.sendMessage(ChatMessageType.ACTION_BAR,
                        new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));

                this.clearRender();
                break;
            }
            case TITLE: {
                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.title(new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);

                this.clearRender();
                break;
            }
            case SUBTITLE: {
                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.subTitle(new TextComponent(StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))));
                titleBuilder.fadeIn(this.getTitleFadeIn());
                titleBuilder.stay(this.getTitleStay());
                titleBuilder.fadeOut(this.getTitleFadeOut());

                player.sendTitle(titleBuilder);

                this.clearRender();
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

                this.clearRender();
                break;
            }
            default: {
                this.clearRender();

                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
            }
        }
    }
}
