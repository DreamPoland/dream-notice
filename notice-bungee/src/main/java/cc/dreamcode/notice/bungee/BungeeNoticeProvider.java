package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.NoticeExecutor;
import cc.dreamcode.notice.NoticeScheduler;
import cc.dreamcode.notice.adventure.AdventureNoticeProvider;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.function.Function;

public class BungeeNoticeProvider<I> extends AdventureNoticeProvider<I> {

    public BungeeNoticeProvider(I messageConfig, Plugin plugin) {
        this(messageConfig, plugin, null);
    }

    public BungeeNoticeProvider(I messageConfig, Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        super(messageConfig, new BungeeNoticeExecutor(audienceAdapter), new BungeeNoticeScheduler(plugin));
    }

    public BungeeNoticeProvider(I messageConfig, NoticeScheduler noticeScheduler) {
        super(messageConfig, new BungeeNoticeExecutor(), noticeScheduler);
    }

    public BungeeNoticeProvider(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        super(messageConfig, noticeExecutor, noticeScheduler);
    }

    public static BungeeNoticeProvider<Object> create(@NonNull Plugin plugin) {
        return new BungeeNoticeProvider<>(null, plugin);
    }

    public static BungeeNoticeProvider<Object> create(@NonNull Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        return new BungeeNoticeProvider<>(null, plugin, audienceAdapter);
    }

    public static <I> BungeeNoticeProvider<I> create(I messageConfig, @NonNull Plugin plugin) {
        return new BungeeNoticeProvider<>(messageConfig, plugin);
    }

    public static <I> BungeeNoticeProvider<I> create(I messageConfig, @NonNull Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        return new BungeeNoticeProvider<>(messageConfig, plugin, audienceAdapter);
    }

    public static <I> BungeeNoticeProvider<I> create(I messageConfig, NoticeScheduler noticeScheduler) {
        return new BungeeNoticeProvider<>(messageConfig, noticeScheduler);
    }

    public static <I> BungeeNoticeProvider<I> create(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        return new BungeeNoticeProvider<>(messageConfig, noticeExecutor, noticeScheduler);
    }
}
