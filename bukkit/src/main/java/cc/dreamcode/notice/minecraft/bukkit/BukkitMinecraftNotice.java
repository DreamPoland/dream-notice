package cc.dreamcode.notice.minecraft.bukkit;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeException;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.utilities.bukkit.ChatUtil;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BukkitMinecraftNotice extends MinecraftNotice<CommandSender> {

    public BukkitMinecraftNotice(@NonNull MinecraftNoticeType type, @NonNull String text) {
        super(type, text);
    }

    public BukkitMinecraftNotice(@NonNull MinecraftNoticeType type, @NonNull String... texts) {
        super(type, texts);
    }

    public static BukkitMinecraftNotice of(@NonNull MinecraftNoticeType type, @NonNull String... texts) {
        return new BukkitMinecraftNotice(type, texts);
    }

    @Override
    public void send(@NonNull CommandSender sender) {
        this.sendFormatted(sender, this.getText());
    }

    @Override
    public void send(@NonNull Collection<CommandSender> senders) {
        senders.forEach(this::send);
    }

    @Override
    public void send(@NonNull CommandSender sender, @NonNull Map<String, Object> mapReplacer) {
        final CompiledMessage compiledMessage = CompiledMessage.of(this.getText());
        final PlaceholderContext placeholderContext = PlaceholderContext.of(compiledMessage);

        this.sendFormatted(sender, placeholderContext
                .with(mapReplacer)
                .apply());
    }

    @Override
    public void send(@NonNull Collection<CommandSender> senders, @NonNull Map<String, Object> mapReplacer) {
        senders.forEach(sender -> this.send(sender, mapReplacer));
    }

    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        Bukkit.getOnlinePlayers().forEach(player -> this.send(player, mapReplacer));
    }

    @Override
    public void sendAllWithPermission(@NonNull String permission) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(this::send);
    }

    @Override
    public void sendAllWithPermission(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> this.send(player, mapReplacer));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull String message) {
        if (!(sender instanceof Player)) {
            String[] split = message.split(MinecraftNotice.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    sender.sendMessage(ChatUtil.fixColor(text)));
            return;
        }

        final Player player = (Player) sender;
        switch (this.getType()) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                String[] split = message.split(MinecraftNotice.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(ChatUtil.fixColor(text)));
                break;
            }
            case ACTION_BAR: {
                ActionBar.sendActionBar(player, ChatUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), "")));
                break;
            }
            case TITLE: {
                Titles.sendTitle(player, ChatUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), "")), "");
                break;
            }
            case SUBTITLE: {
                Titles.sendTitle(player, "", ChatUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), "")));
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = message.split(MinecraftNotice.lineSeparator());
                if (split.length == 0) {
                    throw new MinecraftNoticeException("Notice with TITLE_SUBTITLE need have " + MinecraftNotice.lineSeparator() + " to include title with subtitle.");
                }

                final String title = ChatUtil.fixColor(split[0]);
                final String subTitle = ChatUtil.fixColor(split[1]);

                Titles.sendTitle(player, title, subTitle);
                break;
            }
            default:
                throw new MinecraftNoticeException("Notice type cannot define. (" + this.getType() + ")");
        }
    }
}
