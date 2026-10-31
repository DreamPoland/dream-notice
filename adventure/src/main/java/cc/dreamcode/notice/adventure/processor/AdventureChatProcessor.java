package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.ComponentUtil;
import cc.dreamcode.utilities.adventure.TranslationUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class AdventureChatProcessor implements NoticeProcessor<Audience> {

    private static final Pattern NEWLINE_PATTERN = Pattern.compile("(?i)" + Pattern.quote(Notice.LINE_SEPARATOR) + "|\\{newline\\}");

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

        List<String> preparedLines = Notice.prepareText(textLines);
        List<Component> components = ComponentUtil.ofList(preparedLines, locale, replacements);
        for (Component component : components) {
            Component rendered = TranslationUtil.render(component, locale, replacements);
            rendered = rendered.replaceText(builder -> builder
                    .match(NEWLINE_PATTERN)
                    .replacement(Component.newline()));
            handler.sendMessage(rendered);
        }
    }
}
