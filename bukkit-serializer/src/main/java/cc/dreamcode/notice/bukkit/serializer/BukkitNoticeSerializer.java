package cc.dreamcode.notice.bukkit.serializer;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.notice.serializer.AbstractNoticeSerializer;
import lombok.NonNull;

import java.util.List;

public class BukkitNoticeSerializer extends AbstractNoticeSerializer<BukkitNotice> {

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return BukkitNotice.class.isAssignableFrom(type);
    }

    @Override
    protected BukkitNotice createChat(@NonNull List<String> text) {
        return BukkitNotice.chat(text);
    }

    @Override
    protected BukkitNotice createFromNotice(@NonNull Notice<?> notice) {
        return BukkitNotice.of(notice);
    }
}
