package cc.dreamcode.notice.minecraft.adventure.bukkit;

import cc.dreamcode.notice.DreamSender;
import lombok.NonNull;
import org.bukkit.command.CommandSender;

import java.util.Map;

public interface AdventureBukkitSender extends DreamSender<CommandSender> {

    void sendAll();

    void sendAll(@NonNull Map<String, Object> mapReplacer);

    void sendPermitted(@NonNull String permission);

    void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer);
}
