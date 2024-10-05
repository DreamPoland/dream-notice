package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.adventure.AdventureUtil;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Getter
public class BukkitNotice extends Notice<BukkitNotice> implements BukkitSender {

    private final List<Function<Component, Component>> componentBuilder = new ArrayList<>();

    public BukkitNotice(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static BukkitNotice of(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        return new BukkitNotice(noticeType, noticeText);
    }

    public static BukkitNotice chat(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.CHAT, noticeText);
    }

    public static BukkitNotice actionBar(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.ACTION_BAR, noticeText);
    }

    public static BukkitNotice title(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.TITLE, noticeText);
    }

    public static BukkitNotice subtitle(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.SUBTITLE, noticeText);
    }

    public static BukkitNotice titleSubtitle(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }

    public BukkitNotice apply(@NonNull Function<Component, Component> function) {
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
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.clearRender();
    }

    @Override
    public void sendBroadcast() {
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.wrapAndSend(Bukkit.getConsoleSender());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::wrapAndSend);
        this.wrapAndSend(Bukkit.getConsoleSender());
        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::wrapAndSend);

        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::wrapAndSend);

        this.clearRender();
    }

    private void wrapAndSend(@NonNull CommandSender target) {
        final BukkitAudiences bukkitAudiences = BukkitNoticeProvider.getInstance().getBukkitAudiences();
        this.sendFormatted(target, bukkitAudiences.sender(target));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target) {
        if (!(sender instanceof Player)) {
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
