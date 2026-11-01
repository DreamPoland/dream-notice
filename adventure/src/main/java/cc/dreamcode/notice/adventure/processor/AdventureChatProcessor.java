package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.StringUtil;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import cc.dreamcode.utilities.adventure.TranslationUtil;
import eu.okaeri.placeholders.ast.EvaluationResult;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class AdventureChatProcessor implements NoticeProcessor<Audience> {

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("(?i)" + Pattern.quote(Notice.LINE_SEPARATOR) + "|\\{newline\\}");
    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");
    private static final Pattern PERCENT_PATTERN = Pattern.compile("%(?<content>[a-zA-Z0-9_.-]+)%");

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "CHAT".equals(type) || "MESSAGE".equals(type) || "SIDEBAR".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        List<String> textLines = notice.getText();
        if (textLines == null || textLines.isEmpty()) {
            return;
        }

        Map<String, Object> effectiveReplacements = new HashMap<>();
        if (notice.getReplacements() != null && !notice.getReplacements().isEmpty()) {
            effectiveReplacements.putAll(notice.getReplacements());
        }
        if (replacements != null && !replacements.isEmpty()) {
            effectiveReplacements.putAll(replacements);
        }

        for (String rawLine : textLines) {
            Component component = AdventureUtil.component(rawLine);
            Component template = component.replaceText(builder -> builder
                    .match(NEWLINE_PATTERN)
                    .replacement(Component.newline()));
            Component translated = TranslationUtil.render(template, locale);

            if (effectiveReplacements.isEmpty()) {
                handler.sendMessage(translated);
                continue;
            }

            Map<String, Object> normalized = new HashMap<>();
            for (Map.Entry<String, Object> entry : effectiveReplacements.entrySet()) {
                String key = entry.getKey();
                if ((key.startsWith("{") && key.endsWith("}")) || (key.startsWith("%") && key.endsWith("%"))) {
                    key = key.substring(1, key.length() - 1);
                }
                normalized.put(key, entry.getValue());
            }

            CompiledMessage compiledMessage = CompiledMessage.of(locale, rawLine);
            PlaceholderContext context = StringUtil.getPlaceholders().context(compiledMessage);
            context.with(normalized);
            Map<String, EvaluationResult> fieldResults = context.renderFieldResults();

            Component rendered = translated.replaceText(builder -> builder
                    .match(FIELD_PATTERN)
                    .replacement((result, input) -> {
                        String content = result.group("content");
                        EvaluationResult eval = fieldResults.get(content);
                        if (eval instanceof EvaluationResult.Value) {
                            Object raw = normalized.get(content);
                            if (raw instanceof ComponentLike cl) {
                                return cl.asComponent();
                            }
                            return Component.text(eval.format());
                        } else if (eval instanceof EvaluationResult.NullValue) {
                            return Component.empty();
                        }

                        Object raw = normalized.get(content);
                        if (raw instanceof ComponentLike cl) {
                            return cl.asComponent();
                        }
                        if (raw != null) {
                            return Component.text(String.valueOf(raw));
                        }

                        return Component.text(result.group());
                    }));

            rendered = rendered.replaceText(builder -> builder
                    .match(PERCENT_PATTERN)
                    .replacement((result, input) -> {
                        String content = result.group("content");
                        Object raw = normalized.get(content);
                        if (raw instanceof ComponentLike cl) {
                            return cl.asComponent();
                        }
                        if (raw != null) {
                            return Component.text(String.valueOf(raw));
                        }
                        return Component.text(result.group());
                    }));

            handler.sendMessage(rendered);
        }
    }
}
