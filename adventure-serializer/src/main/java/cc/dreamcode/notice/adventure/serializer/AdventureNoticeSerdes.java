package cc.dreamcode.notice.adventure.serializer;

import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;

public class AdventureNoticeSerdes implements OkaeriSerdesPack {

    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.register(new AdventureNoticeSerializer());
    }
}
