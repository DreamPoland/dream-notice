package cc.dreamcode.notice.minecraft.adventure;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class AdventureLegacy {

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.legacyAmpersand();

    public static Component component(String text) {
        return AMPERSAND_SERIALIZER.deserialize(text);
    }
}
