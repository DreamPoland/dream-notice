package cc.dreamcode.notice.paper;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.utilities.bukkit.adventure.ColorProcessor;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
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
public class PaperNotice extends Notice<PaperNotice> implements PaperSender {

    private final List<Function<Component, Component>> componentBuilder = new ArrayList<>();

    public PaperNotice(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static PaperNotice of(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        return new PaperNotice(noticeType, noticeText);
    }

    public static PaperNotice chat(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.CHAT, noticeText);
    }

    public static PaperNotice actionBar(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.ACTION_BAR, noticeText);
    }

    public static PaperNotice title(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.TITLE, noticeText);
    }

    public static PaperNotice subtitle(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.SUBTITLE, noticeText);
    }

    public static PaperNotice titleSubtitle(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }

    public PaperNotice apply(@NonNull Function<Component, Component> function) {
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
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders) {
        this.with(mapReplacer);
        this.wrapAndSend(target, colorizePlaceholders);
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
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders) {
        this.with(mapReplacer);
        targets.forEach(target -> this.wrapAndSend(target, colorizePlaceholders));
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
    public void sendAll(@NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(target -> this.wrapAndSend(target, colorizePlaceholders));
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
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(target -> this.wrapAndSend(target, colorizePlaceholders));
        this.wrapAndSend(Bukkit.getConsoleSender(), colorizePlaceholders);
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

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders) {
        this.with(mapReplacer);

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(target -> this.wrapAndSend(target, colorizePlaceholders));

        this.clearRender();
    }

    private void wrapAndSend(@NonNull CommandSender target) {
        wrapAndSend(target, true);
    }

    private void wrapAndSend(@NonNull CommandSender target, boolean colorizePlaceholders) {
        this.sendFormatted(target, target, colorizePlaceholders);
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target, boolean colorizePlaceholders) {
        if (!(sender instanceof Player)) {
            Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text -> {

                Component component = this.placeholdersExists()
                        ? ColorProcessor.component(text, this.getPlaceholders(), colorizePlaceholders)
                        : ColorProcessor.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                target.sendMessage(component);
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
                            ? ColorProcessor.component(text, this.getPlaceholders(), colorizePlaceholders)
                            : ColorProcessor.component(text);

                    for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                        component = componentConsumer.apply(component);
                    }

                    target.sendMessage(component);
                });
                break;
            }
            case ACTION_BAR: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Component component = this.placeholdersExists()
                        ? ColorProcessor.component(text, this.getPlaceholders(), colorizePlaceholders)
                        : ColorProcessor.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                target.sendActionBar(component);
                break;
            }
            case TITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");

                Component component = this.placeholdersExists()
                        ? ColorProcessor.component(text, this.getPlaceholders(), colorizePlaceholders)
                        : ColorProcessor.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                Title titleBuilder = Title.title(
                        component,
                        ColorProcessor.component(""),
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
                        ? ColorProcessor.component(text, this.getPlaceholders(), colorizePlaceholders)
                        : ColorProcessor.component(text);

                for (Function<Component, Component> componentConsumer : this.componentBuilder) {
                    component = componentConsumer.apply(component);
                }

                Title titleBuilder = Title.title(
                        ColorProcessor.component(""),
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
                    title = ColorProcessor.component(split[0], this.getPlaceholders(), colorizePlaceholders);
                    subTitle = ColorProcessor.component(split[1], this.getPlaceholders(), colorizePlaceholders);
                }
                else {
                    title = ColorProcessor.component(split[0]);
                    subTitle = ColorProcessor.component(split[1]);
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
