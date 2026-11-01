package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.AudienceUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class AdventureTitleProcessor implements NoticeProcessor<Audience> {

    private static final Pattern CLEAN_PATTERN = Pattern.compile("(?i)" + Pattern.quote(Notice.LINE_SEPARATOR) + "|\\{newline\\}|\\r?\\n");

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "TITLE".equals(type) || "SUBTITLE".equals(type) || "TITLE_SUBTITLE".equals(type) || "TITLE_AND_SUBTITLE".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        String title = notice.getTitle() != null ? notice.getTitle() : "";
        String subtitle = notice.getSubtitle() != null ? notice.getSubtitle() : "";

        if (supportsTitleSubtitle(notice.getNoticeType()) || (subtitle.isEmpty() && Notice.containsLineSeparator(title))) {
            if (Notice.containsLineSeparator(title)) {
                String[] split = Notice.splitLineSeparator(title, 2);
                title = split[0];
                if (subtitle.isEmpty() && split.length > 1) {
                    subtitle = split[1];
                }
            } else if (title.isEmpty() && notice.getText() != null && !notice.getText().isEmpty()) {
                if (notice.getText().size() >= 2) {
                    title = notice.getText().get(0);
                    if (subtitle.isEmpty()) {
                        subtitle = notice.getText().get(1);
                    }
                } else {
                    String single = notice.getText().get(0);
                    if (Notice.containsLineSeparator(single)) {
                        String[] split = Notice.splitLineSeparator(single, 2);
                        title = split[0];
                        if (subtitle.isEmpty() && split.length > 1) {
                            subtitle = split[1];
                        }
                    } else {
                        title = single;
                    }
                }
            }
        } else if ("SUBTITLE".equalsIgnoreCase(notice.getNoticeType().getName())) {
            if (subtitle.isEmpty() && !title.isEmpty()) {
                subtitle = title;
                title = "";
            } else if (subtitle.isEmpty() && notice.getText() != null && !notice.getText().isEmpty()) {
                subtitle = notice.getText().get(0);
            }
        } else if (title.isEmpty() && notice.getText() != null && !notice.getText().isEmpty()) {
            title = notice.getText().get(0);
        }

        title = cleanLineSeparators(title);
        subtitle = cleanLineSeparators(subtitle);

        Map<String, Object> effectiveReplacements = new HashMap<>();
        if (notice.getReplacements() != null && !notice.getReplacements().isEmpty()) {
            effectiveReplacements.putAll(notice.getReplacements());
        }
        if (replacements != null && !replacements.isEmpty()) {
            effectiveReplacements.putAll(replacements);
        }

        AudienceUtil.sendTitle(handler, title, subtitle, effectiveReplacements, notice.getFadeIn(), notice.getStay(), notice.getFadeOut());
    }

    private boolean supportsTitleSubtitle(NoticeType noticeType) {
        if (noticeType == null) {
            return false;
        }
        String type = noticeType.getName().toUpperCase();
        return "TITLE_SUBTITLE".equals(type) || "TITLE_AND_SUBTITLE".equals(type);
    }

    private String cleanLineSeparators(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return CLEAN_PATTERN.matcher(text).replaceAll("");
    }
}
