package cc.dreamcode.notice.serializer;

import cc.dreamcode.notice.Notice;
import lombok.NonNull;

import java.util.List;

public class NoticeSerializer extends AbstractNoticeSerializer<Notice<?>> {

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return Notice.class.isAssignableFrom(type);
    }

    @Override
    protected Notice<?> createChat(@NonNull List<String> text) {
        return Notice.chat(text);
    }

    @Override
    protected Notice<?> createFromNotice(@NonNull Notice<?> notice) {
        return notice;
    }
}
