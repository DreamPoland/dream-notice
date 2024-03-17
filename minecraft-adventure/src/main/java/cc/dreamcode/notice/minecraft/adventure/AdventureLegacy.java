package cc.dreamcode.notice.minecraft.adventure;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Arrays;
import java.util.List;

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

    public static List<Component> splitDeserialize(@NonNull String rawText) {

        final ListBuilder<Component> listBuilder = new ListBuilder<>();

        String[] split = rawText.split(MinecraftNotice.lineSeparator());
        Arrays.stream(split).forEach(text -> listBuilder.add(AdventureLegacy.deserialize(text)));

        return listBuilder.build();
    }

    public static Component joiningDeserialize(@NonNull String rawText) {

        final String joiningText = StringUtil.join(rawText.split(MinecraftNotice.lineSeparator()), " ");
        return AdventureLegacy.deserialize(joiningText);
    }

    public static Component deserialize(@NonNull String text) {
        return MINI_MESSAGE.deserialize(text);
    }
}
