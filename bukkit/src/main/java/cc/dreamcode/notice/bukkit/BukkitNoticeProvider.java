package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.NoticeExecutor;
import cc.dreamcode.notice.NoticeScheduler;
import cc.dreamcode.notice.adventure.AdventureNoticeProvider;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.function.Function;

public class BukkitNoticeProvider<I> extends AdventureNoticeProvider<I> {

    public BukkitNoticeProvider(I messageConfig, Plugin plugin) {
        this(messageConfig, plugin, null);
    }

    public BukkitNoticeProvider(I messageConfig, Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        super(messageConfig, new BukkitNoticeExecutor(audienceAdapter), new BukkitNoticeScheduler(plugin));
    }

    public BukkitNoticeProvider(I messageConfig, NoticeScheduler noticeScheduler) {
        super(messageConfig, new BukkitNoticeExecutor(), noticeScheduler);
    }

    public BukkitNoticeProvider(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        super(messageConfig, noticeExecutor, noticeScheduler);
    }

    public static BukkitNoticeProvider<Object> create(@NonNull Plugin plugin) {
        return new BukkitNoticeProvider<>(null, plugin);
    }

    public static BukkitNoticeProvider<Object> create(@NonNull Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        return new BukkitNoticeProvider<>(null, plugin, audienceAdapter);
    }

    public static <I> BukkitNoticeProvider<I> create(I messageConfig, @NonNull Plugin plugin) {
        return new BukkitNoticeProvider<>(messageConfig, plugin);
    }

    public static <I> BukkitNoticeProvider<I> create(I messageConfig, @NonNull Plugin plugin, Function<CommandSender, Audience> audienceAdapter) {
        return new BukkitNoticeProvider<>(messageConfig, plugin, audienceAdapter);
    }

    public static <I> BukkitNoticeProvider<I> create(I messageConfig, NoticeScheduler noticeScheduler) {
        return new BukkitNoticeProvider<>(messageConfig, noticeScheduler);
    }

    public static <I> BukkitNoticeProvider<I> create(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        return new BukkitNoticeProvider<>(messageConfig, noticeExecutor, noticeScheduler);
    }
}
