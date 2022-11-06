package cc.dreamcode.notice.bukkit.legacy;

import lombok.NoArgsConstructor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.stream.Collectors;

@NoArgsConstructor
public final class Legacy {
    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    public static Component component(String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }

    public static String serialize(Component component) {
        return AMPERSAND_SERIALIZER.serialize(component);
    }
}
