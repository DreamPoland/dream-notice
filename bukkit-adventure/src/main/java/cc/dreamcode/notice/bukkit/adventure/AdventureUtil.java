package cc.dreamcode.notice.bukkit.adventure;

import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdventureUtil {

    private static final char COLOR_CHAR = '&';

    private static final Pattern ALL_TEXT_PATTERN = Pattern.compile(".*");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");
    private static final Pattern SECTION_COLOR_PATTERN = Pattern.compile("(?i)§([0-9A-FK-OR])");

    private static final LegacyComponentSerializer AMPERSAND_SERIALIZER = LegacyComponentSerializer.builder()
            .character(COLOR_CHAR)
            .hexColors()
            .build();

    private static final TextReplacementConfig COLOR_REPLACEMENTS = TextReplacementConfig.builder()
            .match(ALL_TEXT_PATTERN)
            .replacement((result, input) -> Component.text(StringColorUtil.legacyFixColor(result.group())))
            .build();

    private static final MiniMessage MINI_MESSAGE = MiniMessage.builder()
            .preProcessor(text -> SECTION_COLOR_PATTERN.matcher(text).replaceAll("&$1"))
            .postProcessor(component -> component.replaceText(COLOR_REPLACEMENTS))
            .build();
    private static final MiniMessage PLACEHOLDER_MINI_MESSAGE = MiniMessage.builder()
            .preProcessor(text -> SECTION_COLOR_PATTERN.matcher(text).replaceAll("&$1"))
            .tags(TagResolver.builder()
                    .resolver(StandardTags.color())
                    .resolver(StandardTags.decorations())
                    .resolver(StandardTags.rainbow())
                    .resolver(StandardTags.gradient())
                    .resolver(StandardTags.transition())
                    .build())
            .postProcessor(component -> component.replaceText(COLOR_REPLACEMENTS))
            .build();

    public static Component component(@NonNull String text) {
        return MINI_MESSAGE.deserialize(text);
    }

    public static Component component(@NonNull String text, @NonNull Map<String, Object> placeholders) {
        return component(text, Locale.forLanguageTag("pl"), placeholders);
    }

    public static Component component(@NonNull String text, @NonNull Locale locale, @NonNull Map<String, Object> placeholders) {
        final CompiledMessage compiledMessage = CompiledMessage.of(locale, text);
        final PlaceholderContext placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage)
                .with(placeholders);

        return component(text, placeholderContext);
    }

    public static Component component(@NonNull String text, @NonNull PlaceholderContext placeholderContext) {

        Component component = MINI_MESSAGE.deserialize(text);

        final Map<String, String> fields = renderFields(placeholderContext);
        final TextReplacementConfig replacementConfig = replacementConfig(fields);

        return component.replaceText(replacementConfig);
    }

    private static Map<String, String> renderFields(@NonNull PlaceholderContext placeholderContext) {
        return placeholderContext.renderFields()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getRaw(),
                        entry -> {
                            final Component component = PLACEHOLDER_MINI_MESSAGE.deserialize(entry.getValue());
                            return AMPERSAND_SERIALIZER.serialize(component);
                        }
                ));
    }

    private static TextReplacementConfig replacementConfig(@NonNull Map<String, String> replaceMap) {
        return TextReplacementConfig.builder()
                .match(FIELD_PATTERN)
                .replacement((result, input) -> {
                    final String value = replaceMap.get(result.group(1));
                    return AMPERSAND_SERIALIZER.deserialize(value);
                })
                .build();
    }
}
