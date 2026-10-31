package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.adventure.AdventureNoticeAudience;
import cc.dreamcode.notice.bungee.adapter.BungeeAudienceAdapter;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Locale;
import java.util.function.Function;

@Getter
public class BungeeNoticeAudience implements NoticeAudience<CommandSender> {

    private final CommandSender sender;
    private final Audience audience;
    private final String name;
    private final Type type;

    public BungeeNoticeAudience(@NonNull CommandSender sender) {
        this(sender, null);
    }

    public BungeeNoticeAudience(@NonNull CommandSender sender, Function<CommandSender, Audience> customAdapter) {
        this.sender = sender;
        this.name = sender.getName();
        this.type = (sender instanceof ProxiedPlayer) ? Type.CLIENT : Type.CONSOLE;
        if (customAdapter != null) {
            this.audience = customAdapter.apply(sender);
        } else if (sender instanceof Audience aud) {
            this.audience = aud;
        } else {
            this.audience = new BungeeAudienceAdapter(sender);
        }
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return this.sender.hasPermission(permission);
    }

    @Override
    public CommandSender getHandler() {
        return this.sender;
    }

    @Override
    public Locale getLocale() {
        if (this.sender instanceof ProxiedPlayer player) {
            return player.getLocale() != null ? player.getLocale() : Locale.getDefault();
        }
        return Locale.getDefault();
    }

    public NoticeAudience<Audience> asAdventureAudience() {
        return new AdventureNoticeAudience(this.audience, this.name, this.type);
    }
}
