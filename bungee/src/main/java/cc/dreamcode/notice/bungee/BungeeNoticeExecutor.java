package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.adventure.AdventureNoticeExecutor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class BungeeNoticeExecutor extends AdventureNoticeExecutor {

    @Getter
    @Setter
    private Function<CommandSender, Audience> audienceAdapter;

    public BungeeNoticeExecutor() {
        this(null);
    }

    public BungeeNoticeExecutor(Function<CommandSender, Audience> audienceAdapter) {
        this.audienceAdapter = audienceAdapter;
    }

    @Override
    public void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> noticeAudience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        super.send(notice, noticeAudience, locale, replacements);
    }

    @Override
    public NoticeAudience<?> resolveAudience(@NonNull Object target) {
        if (target instanceof BungeeNoticeAudience bungeeAudience) {
            return bungeeAudience.asAdventureAudience();
        }
        if (target instanceof CommandSender sender) {
            return new BungeeNoticeAudience(sender, this.audienceAdapter).asAdventureAudience();
        }
        return super.resolveAudience(target);
    }

    @Override
    public Collection<NoticeAudience<?>> getAllAudiences() {
        List<NoticeAudience<?>> audiences = new ArrayList<>();
        ProxyServer proxy = ProxyServer.getInstance();
        if (proxy != null) {
            for (ProxiedPlayer player : proxy.getPlayers()) {
                audiences.add(new BungeeNoticeAudience(player, this.audienceAdapter).asAdventureAudience());
            }
            CommandSender console = proxy.getConsole();
            if (console != null) {
                audiences.add(new BungeeNoticeAudience(console, this.audienceAdapter).asAdventureAudience());
            }
        }
        return audiences;
    }
}
