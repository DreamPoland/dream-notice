package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.NoticeSender;
import lombok.NonNull;
import net.md_5.bungee.api.CommandSender;

import java.util.Map;

public interface BungeeSender extends NoticeSender<CommandSender> {

    void sendAll();

    void sendAll(@NonNull Map<String, Object> mapReplacer);

    void sendPermitted(@NonNull String permission);

    void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer);
}
