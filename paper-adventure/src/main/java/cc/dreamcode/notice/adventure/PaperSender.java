package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.NoticeSender;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface PaperSender extends NoticeSender<CommandSender> {

    void sendAll();

    void sendAll(@NonNull Map<String, Object> mapReplacer);

    void sendPermitted(@NonNull String permission);

    void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer);
}
