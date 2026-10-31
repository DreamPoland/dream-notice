package cc.dreamcode.notice.bungee.serializer;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.bungee.BungeeNotice;
import cc.dreamcode.notice.serializer.AbstractNoticeSerializer;
import lombok.NonNull;

import java.util.List;

public class BungeeNoticeSerializer extends AbstractNoticeSerializer<BungeeNotice> {

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return BungeeNotice.class.isAssignableFrom(type);
    }

    @Override
    protected BungeeNotice createChat(@NonNull List<String> text) {
        return BungeeNotice.chat(text);
    }

    @Override
    protected BungeeNotice createFromNotice(@NonNull Notice<?> notice) {
        return BungeeNotice.of(notice);
    }
}
