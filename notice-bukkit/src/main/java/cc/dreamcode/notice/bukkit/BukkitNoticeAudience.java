package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.adventure.AdventureNoticeAudience;
import cc.dreamcode.notice.bukkit.adapter.SpigotAudienceAdapter;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.function.Function;

@Getter
public class BukkitNoticeAudience implements NoticeAudience<CommandSender> {

    private final CommandSender sender;
    private final Audience audience;
    private final String name;
    private final Type type;

    public BukkitNoticeAudience(@NonNull CommandSender sender) {
        this(sender, null);
    }

    public BukkitNoticeAudience(@NonNull CommandSender sender, Function<CommandSender, Audience> customAdapter) {
        this.sender = sender;
        this.name = sender.getName();
        this.type = (sender instanceof ConsoleCommandSender) ? Type.CONSOLE : Type.CLIENT;
        if (customAdapter != null) {
            this.audience = customAdapter.apply(sender);
        } else if (sender instanceof Audience aud) {
            this.audience = aud;
        } else {
            this.audience = new SpigotAudienceAdapter(sender);
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
        if (this.sender instanceof Player player) {
            try {
                Locale loc = player.locale();
                if (loc != null) {
                    return loc;
                }
            } catch (Throwable ignored) {
            }
            try {
                String loc = player.getLocale();
                if (loc != null && !loc.isEmpty()) {
                    String[] parts = loc.split("_");
                    if (parts.length >= 2) {
                        return new Locale(parts[0], parts[1]);
                    }
                    return new Locale(parts[0]);
                }
            } catch (Throwable ignored) {
            }
        }
        return Locale.getDefault();
    }

    public NoticeAudience<Audience> asAdventureAudience() {
        return new AdventureNoticeAudience(this.audience, this.name, this.type);
    }
}
