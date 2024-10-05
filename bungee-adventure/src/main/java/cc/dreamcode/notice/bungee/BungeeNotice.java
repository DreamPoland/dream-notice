package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bungee.adventure.AdventureUtil;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public class BungeeNotice extends Notice<BungeeNotice> implements BungeeSender {

    private final List<Function<Component, Component>> componentBuilder = new ArrayList<>();

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
        return new BungeeNotice(NoticeType.SUBTITLE, noticeText);
    }

    public static BungeeNotice titleSubtitle(@NonNull String... noticeText) {
        return new BungeeNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }

    public BungeeNotice apply(@NonNull Function<Component, Component> function) {
        this.componentBuilder.add(function);
        return this;
    }

    @Override
    public void send(@NonNull CommandSender target) {
        this.wrapAndSend(target);

        this.clearRender();
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        this.wrapAndSend(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        targets.forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendBroadcast() {
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.wrapAndSend(ProxyServer.getInstance().getConsole());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        ProxyServer.getInstance().getPlayers().forEach(this::wrapAndSend);
        this.wrapAndSend(ProxyServer.getInstance().getConsole());
        this.clearRender();
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
        this.with(mapReplacer);

        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::wrapAndSend);

        this.clearRender();
    }

    private void wrapAndSend(@NonNull CommandSender target) {
        final BungeeAudiences bungeeAudiences = BungeeNoticeProvider.getInstance().getBungeeAudiences();
        this.sendFormatted(target, bungeeAudiences.sender(target));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target) {
        if (!(sender instanceof ProxiedPlayer)) {
            Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text -> {

                Component component = this.placeholdersExists()
                        ? AdventureUtil.component(text, this.getPlaceholders())
                        : AdventureUtil.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                target.sendMessage(this.placeholdersExists()
                        ? AdventureUtil.component(text, this.getPlaceholders())
                        : AdventureUtil.component(text));
            });
            return;
        }

        final NoticeType noticeType = this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text -> {

                    Component component = this.placeholdersExists()
                            ? AdventureUtil.component(text, this.getPlaceholders())
                            : AdventureUtil.component(text);

                    for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                        component = componentConsumer.apply(component);
                    }

                    target.sendMessage(this.placeholdersExists()
                            ? AdventureUtil.component(text, this.getPlaceholders())
                            : AdventureUtil.component(text));
                });
                break;
            }
            case ACTION_BAR: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Component component = this.placeholdersExists()
                        ? AdventureUtil.component(text, this.getPlaceholders())
                        : AdventureUtil.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                target.sendActionBar(component);
                break;
            }
            case TITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Component component = this.placeholdersExists()
                        ? AdventureUtil.component(text, this.getPlaceholders())
                        : AdventureUtil.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                Title titleBuilder = Title.title(
                        component,
                        AdventureUtil.component(""),
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
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Component component = this.placeholdersExists()
                        ? AdventureUtil.component(text, this.getPlaceholders())
                        : AdventureUtil.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                Title titleBuilder = Title.title(
                        AdventureUtil.component(""),
                        component,
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
                String[] split = this.getNoticeText().split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + Notice.lineSeparator() + ") to separate two messages.");
                }

                Component title;
                Component subTitle;

                if (this.placeholdersExists()) {
                    title = AdventureUtil.component(split[0], this.getPlaceholders());
                    subTitle = AdventureUtil.component(split[1], this.getPlaceholders());
                }
                else {
                    title = AdventureUtil.component(split[0]);
                    subTitle = AdventureUtil.component(split[1]);
                }

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    title = componentConsumer.apply(title);
                    subTitle = componentConsumer.apply(subTitle);
                }

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
