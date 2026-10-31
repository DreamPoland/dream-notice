package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.adventure.AdventureNoticeExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class BukkitNoticeExecutor extends AdventureNoticeExecutor {

    @Getter
    @Setter
    private Function<CommandSender, Audience> audienceAdapter;

    public BukkitNoticeExecutor() {
        this(null);
    }

    public BukkitNoticeExecutor(Function<CommandSender, Audience> audienceAdapter) {
        this.audienceAdapter = audienceAdapter;
    }

    @Override
    public void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> noticeAudience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        super.send(notice, noticeAudience, locale, replacements);
    }

    @Override
    public NoticeAudience<?> resolveAudience(@NonNull Object target) {
        if (target instanceof BukkitNoticeAudience bukkitAudience) {
            return bukkitAudience.asAdventureAudience();
        }
        if (target instanceof CommandSender sender) {
            return new BukkitNoticeAudience(sender, this.audienceAdapter).asAdventureAudience();
        }
        return super.resolveAudience(target);
    }

    @Override
    public Collection<NoticeAudience<?>> getAllAudiences() {
        List<NoticeAudience<?>> audiences = new ArrayList<>();
        try {
            for (CommandSender sender : Bukkit.getOnlinePlayers()) {
                audiences.add(new BukkitNoticeAudience(sender, this.audienceAdapter).asAdventureAudience());
            }
            CommandSender console = Bukkit.getConsoleSender();
            if (console != null) {
                audiences.add(new BukkitNoticeAudience(console, this.audienceAdapter).asAdventureAudience());
            }
        } catch (Throwable ignored) {
        }
        return audiences;
    }
}
