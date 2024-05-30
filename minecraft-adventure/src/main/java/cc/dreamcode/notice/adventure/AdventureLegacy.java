package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
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
        return splitDeserialize(rawText, null);
    }

    public static List<Component> splitDeserialize(@NonNull String rawText, TextReplacementConfig textReplacementConfig) {

        final ListBuilder<Component> listBuilder = new ListBuilder<>();

        String[] split = rawText.split(NoticeImpl.lineSeparator());
        Arrays.stream(split).forEach(text -> listBuilder.add(deserialize(text, textReplacementConfig)));

        return listBuilder.build();
    }

    public static Component joiningDeserialize(@NonNull String rawText) {
        return joiningDeserialize(rawText, null);
    }

    public static Component joiningDeserialize(@NonNull String rawText, TextReplacementConfig textReplacementConfig) {

        final String joiningText = StringUtil.join(rawText.split(NoticeImpl.lineSeparator()), " ");
        return deserialize(joiningText, textReplacementConfig);
    }

    public static Component deserialize(@NonNull String text) {
        return deserialize(text, null);
    }

    public static Component deserialize(@NonNull String text, TextReplacementConfig textReplacementConfig) {

        Component component = MINI_MESSAGE.deserialize(text);

        if (textReplacementConfig != null) {
            component = component.replaceText(textReplacementConfig);
        }

        return component;
    }
}
