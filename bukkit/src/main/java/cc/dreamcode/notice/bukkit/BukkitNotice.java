package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeException;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.legacy.Legacy;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BukkitNotice extends Notice<CommandSender> {

    public BukkitNotice(@NonNull NoticeType type, @NonNull String text, int duration) {
        super(type, text, duration);
    }

    public BukkitNotice(@NonNull NoticeType type, @NonNull String text) {
        super(type, text);
    }

    public BukkitNotice(@NonNull NoticeType type, @NonNull String... texts) {
        super(type, texts);
    }

    public static BukkitNotice of(@NonNull NoticeType type, @NonNull String... texts) {
        return new BukkitNotice(type, texts);
    }

    @Override
    public void send(@NonNull CommandSender sender) {
        final Component component = MiniMessage.miniMessage().deserialize(this.getText());
        this.sendFormatted(sender, Legacy.AMPERSAND_SERIALIZER.serialize(component));
    }

    @Override
    public void send(@NonNull Collection<CommandSender> senders) {
        senders.forEach(this::send);
    }

    @Override
    public void send(@NonNull CommandSender sender, @NonNull Map<String, Object> mapReplacer) {
        final Component component = MiniMessage.miniMessage().deserialize(this.getText());
        final String colored = Legacy.AMPERSAND_SERIALIZER.serialize(component);
        final CompiledMessage compiledMessage = CompiledMessage.of(colored);
        final PlaceholderContext placeholderContext = PlaceholderContext.of(compiledMessage);

        this.sendFormatted(sender, placeholderContext
                .with(mapReplacer)
                .apply());
    }

    @Override
    public void send(@NonNull Collection<CommandSender> senders, @NonNull Map<String, Object> mapReplacer) {
        senders.forEach(sender -> this.send(sender, mapReplacer));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull String text) {
        if (!(sender instanceof Player)) {
            String[] split = text.split("%NEWLINE%");
            Arrays.stream(split).forEach(sender::sendMessage);
            return;
        }

        final Player player = (Player) sender;
        switch (this.getType()) {
            case CHAT: {
                String[] split = text.split("%NEWLINE%");
                Arrays.stream(split).forEach(sender::sendMessage);
                break;
            }
            case ACTION_BAR: {
                ActionBar.sendActionBar(player, text.replace(Notice.lineSeparator(), ""));
                break;
            }
            case TITLE: {
                Titles.sendTitle(player, text.replace(Notice.lineSeparator(), ""), " ");
                break;
            }
            case SUBTITLE: {
                Titles.sendTitle(player, " ", text.replace(Notice.lineSeparator(), ""));
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = text.split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new NoticeException("Notice with TITLE_SUBTITLE need have " + Notice.lineSeparator() + " to include title with subtitle.");
                }
                Titles.sendTitle(player, split[0], split[1]);
                break;
            }
            default:
                throw new NoticeException("Notice type cannot define. (" + this.getType() + ")");
        }
    }
}
