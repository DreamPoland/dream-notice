package cc.dreamcode.notice.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import lombok.NonNull;

import java.util.Locale;
import java.util.Map;

public interface NoticeProcessor<A> {

    boolean supports(@NonNull NoticeType noticeType);

    void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<A> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements);
}
