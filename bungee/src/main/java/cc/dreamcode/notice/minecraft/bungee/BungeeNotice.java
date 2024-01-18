package cc.dreamcode.notice.minecraft.bungee;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeException;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.bungee.StringColorUtil;
import lombok.NonNull;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BungeeNotice extends MinecraftNotice<CommandSender> {

    public BungeeNotice(@NonNull MinecraftNoticeType type, @NonNull String text) {
        super(type, text);
    }

    public BungeeNotice(@NonNull MinecraftNoticeType type, @NonNull String... texts) {
        super(type, texts);
    }

    public static BungeeNotice of(@NonNull MinecraftNoticeType type, @NonNull String... texts) {
        return new BungeeNotice(type, texts);
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
        this.sendFormatted(sender, StringUtil.replace(this.getText(), mapReplacer));
    }

    @Override
    public void send(@NonNull Collection<CommandSender> senders, @NonNull Map<String, Object> mapReplacer) {
        senders.forEach(sender -> this.send(sender, mapReplacer));
    }

    @Override
    public void sendAll() {
        ProxyServer.getInstance().getPlayers().forEach(this::send);
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        ProxyServer.getInstance().getPlayers().forEach(player -> this.send(player, mapReplacer));
    }

    @Override
    public void sendAllWithPermission(@NonNull String permission) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(this::send);
    }

    @Override
    public void sendAllWithPermission(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        ProxyServer.getInstance().getPlayers()
                .stream()
                .filter(player -> player.hasPermission(permission))
                .forEach(player -> this.send(player, mapReplacer));
    }

    private void sendFormatted(@NonNull CommandSender sender, @NonNull String message) {
        if (!(sender instanceof ProxiedPlayer)) {
            String[] split = message.split(MinecraftNotice.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    sender.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        switch (this.getType()) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                String[] split = message.split(MinecraftNotice.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(new TextComponent(StringColorUtil.fixColor(text))));
                break;
            }
            case ACTION_BAR: {
                player.sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(StringColorUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), ""))));
                break;
            }
            case TITLE: {
                Title title = ProxyServer.getInstance().createTitle();
                title.title(new TextComponent(StringColorUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), ""))));

                player.sendTitle(title);
                break;
            }
            case SUBTITLE: {
                Title title = ProxyServer.getInstance().createTitle();
                title.subTitle(new TextComponent(StringColorUtil.fixColor(message.replace(MinecraftNotice.lineSeparator(), ""))));

                player.sendTitle(title);
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = message.split(MinecraftNotice.lineSeparator());
                if (split.length == 0) {
                    throw new MinecraftNoticeException("Notice with TITLE_SUBTITLE need have " + MinecraftNotice.lineSeparator() + " to include title with subtitle.");
                }

                final String title = StringColorUtil.fixColor(split[0]);
                final String subTitle = StringColorUtil.fixColor(split[1]);

                Title titleBuilder = ProxyServer.getInstance().createTitle();
                titleBuilder.title(new TextComponent(title));
                titleBuilder.subTitle(new TextComponent(subTitle));

                player.sendTitle(titleBuilder);
                break;
            }
            default:
                throw new MinecraftNoticeException("Notice type cannot define. (" + this.getType() + ")");
        }
    }
}
