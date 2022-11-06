package cc.dreamcode.notice.bukkit.legacy;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

@NoArgsConstructor
public final class Legacy {
    public static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();
}
