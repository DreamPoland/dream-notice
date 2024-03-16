package cc.dreamcode.notice.minecraft.adventure;

import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class AdventureLegacy {

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .character('&')
            .hexColors()
            .useUnusualXRepeatedCharacterHexFormat()
            .build();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .postProcessor(new AdventureLegacyColor())
            .build();

    public static Component component(@NonNull String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }

    public static Component deserialize(@NonNull String text) {
        return MINI_MESSAGE.deserialize(text);
    }
}
