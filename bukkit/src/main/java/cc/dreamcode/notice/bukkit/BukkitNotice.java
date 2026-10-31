package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.adventure.AdventureNotice;
import cc.dreamcode.notice.translation.TranslationRegistry;
import lombok.NonNull;

import java.text.MessageFormat;

public class BukkitNotice extends AdventureNotice {

    public BukkitNotice() {
        super();
    }

    public BukkitNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public BukkitNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }
}
