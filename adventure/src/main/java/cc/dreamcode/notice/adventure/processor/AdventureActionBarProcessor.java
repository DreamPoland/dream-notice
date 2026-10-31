package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.AudienceUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdventureActionBarProcessor implements NoticeProcessor<Audience> {

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "ACTION_BAR".equals(type) || "ACTIONBAR".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        List<String> textLines = notice.getText();
        String message = (textLines != null && !textLines.isEmpty()) ? textLines.get(0) : notice.getTitle();
        if (message == null) {
            return;
        }

        AudienceUtil.sendActionBar(handler, message, replacements);
    }
}
