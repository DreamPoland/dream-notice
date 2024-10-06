package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.NoticeSender;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

import java.util.Map;

public interface BungeeSender extends NoticeSender<CommandSender> {

    void sendAll();

    void sendAll(@NonNull Map<String, Object> mapReplacer);

    void sendAll(@NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders);

    void sendBroadcast();

    void sendBroadcast(@NonNull Map<String, Object> mapReplacer);

    void sendBroadcast(@NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders);

    void sendPermitted(@NonNull String permission);

    void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer);

    void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer, boolean colorizePlaceholders);
}
