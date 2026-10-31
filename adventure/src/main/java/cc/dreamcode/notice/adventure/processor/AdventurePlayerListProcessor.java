package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.PlayerListUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;

import java.util.Locale;
import java.util.Map;

public class AdventurePlayerListProcessor implements NoticeProcessor<Audience> {

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "PLAYER_LIST".equals(type) || "TABLIST".equals(type) || "HEADER_FOOTER".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        String header = notice.getHeader() != null ? notice.getHeader() : notice.getTitle();
        String footer = notice.getFooter() != null ? notice.getFooter() : notice.getSubtitle();

        if (header == null && footer == null) {
            return;
        }

        PlayerListUtil.sendHeaderAndFooter(handler, header, footer, locale, replacements);
    }
}
