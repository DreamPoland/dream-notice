package cc.dreamcode.notice;

import lombok.NonNull;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

public interface NoticeExecutor<A, I extends NoticeExecutor<A, I>> {

    default void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<A> noticeAudience) {
        this.send(notice, noticeAudience, Collections.emptyMap());
    }

    default void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<A> noticeAudience, @NonNull Map<String, Object> replacements) {
        this.send(notice, noticeAudience, noticeAudience.getLocale(), replacements);
    }

    void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<A> noticeAudience, @NonNull Locale locale, @NonNull Map<String, Object> replacements);
}
