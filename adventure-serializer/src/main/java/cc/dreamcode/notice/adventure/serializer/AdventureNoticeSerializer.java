package cc.dreamcode.notice.adventure.serializer;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.adventure.AdventureNotice;
import cc.dreamcode.notice.serializer.AbstractNoticeSerializer;
import lombok.NonNull;

import java.util.List;

public class AdventureNoticeSerializer extends AbstractNoticeSerializer<AdventureNotice> {

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return AdventureNotice.class.isAssignableFrom(type);
    }

    @Override
    protected AdventureNotice createChat(@NonNull List<String> text) {
        return AdventureNotice.chat(text);
    }

    @Override
    protected AdventureNotice createFromNotice(@NonNull Notice<?> notice) {
        return AdventureNotice.of(notice);
    }
}
