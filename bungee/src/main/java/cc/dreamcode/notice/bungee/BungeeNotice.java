package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeException;
import cc.dreamcode.notice.NoticeType;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.platform.AudienceProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BungeeNotice extends Notice<CommandSender> {

    public BungeeNotice(@NonNull NoticeType type, @NonNull String text) {
        super(type, text);
    }

    public BungeeNotice(@NonNull NoticeType type, @NonNull String... texts) {
        super(type, texts);
    }

    public static BungeeNotice of(@NonNull NoticeType type, @NonNull String... texts) {
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
        final MiniMessage miniMessage = BungeeNoticeProvider.getInstance().getMiniMessage();
        final AudienceProvider audienceProvider = BungeeNoticeProvider.getInstance().getAudienceProvider();

        if (!(sender instanceof ProxiedPlayer)) {
            String[] split = message.split(Notice.lineSeparator());
            Arrays.stream(split).forEach(text -> {
                final Component component = miniMessage.deserialize(text);
                audienceProvider.console().sendMessage(component);
            });
            return;
        }

        final ProxiedPlayer player = (ProxiedPlayer) sender;
        switch (this.getType()) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                String[] split = message.split(Notice.lineSeparator());
                Arrays.stream(split).forEach(text -> {
                    final Component component = miniMessage.deserialize(text);
                    audienceProvider.player(player.getUniqueId()).sendMessage(component);
                });
                break;
            }
            case ACTION_BAR: {
                final Component component = miniMessage.deserialize(message);
                audienceProvider.player(player.getUniqueId()).sendActionBar(component);
                break;
            }
            case TITLE: {
                final Component component = miniMessage.deserialize(message);
                final Component emptyComponent = miniMessage.deserialize(" ");

                Title title = Title.title(component, emptyComponent);
                audienceProvider.player(player.getUniqueId()).showTitle(title);
                break;
            }
            case SUBTITLE: {
                final Component component = miniMessage.deserialize(message);
                final Component emptyComponent = miniMessage.deserialize(" ");

                Title title = Title.title(emptyComponent, component);
                audienceProvider.player(player.getUniqueId()).showTitle(title);
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = message.split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new NoticeException("Notice with TITLE_SUBTITLE need have " + Notice.lineSeparator() + " to include title with subtitle.");
                }

                final Component component = miniMessage.deserialize(split[0]);
                final Component subComponent = miniMessage.deserialize(split[1]);

                Title title = Title.title(component, subComponent);
                audienceProvider.player(player.getUniqueId()).showTitle(title);
                break;
            }
            default:
                throw new NoticeException("Notice type cannot define. (" + this.getType() + ")");
        }
    }
}
