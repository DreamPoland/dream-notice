package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.translation.TranslationRegistry;
import lombok.NonNull;

import java.text.MessageFormat;

public class AdventureNotice extends Notice<MessageFormat> {

    public AdventureNotice() {
        super();
    }

    public AdventureNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public AdventureNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }
}
