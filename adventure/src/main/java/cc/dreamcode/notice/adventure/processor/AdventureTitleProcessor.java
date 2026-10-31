package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.AudienceUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;

import java.util.Locale;
import java.util.Map;

public class AdventureTitleProcessor implements NoticeProcessor<Audience> {

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

        if (title.isEmpty() && notice.getText() != null && !notice.getText().isEmpty()) {
            title = notice.getText().get(0);
        }

        AudienceUtil.sendTitle(handler, title, subtitle, replacements, notice.getFadeIn(), notice.getStay(), notice.getFadeOut());
    }
}
