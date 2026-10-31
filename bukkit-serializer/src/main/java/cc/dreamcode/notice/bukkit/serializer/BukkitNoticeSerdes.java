package cc.dreamcode.notice.bukkit.serializer;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class BukkitNoticeSerdes implements OkaeriSerdesPack {

    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new BukkitNoticeSerializer());
    }
}
