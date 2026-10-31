package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.adventure.AdventureNotice;
import cc.dreamcode.notice.translation.TranslationRegistry;
import lombok.NonNull;

import java.text.MessageFormat;

public class BungeeNotice extends AdventureNotice {

    public BungeeNotice() {
        super();
    }

    public BungeeNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public BungeeNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }
}
