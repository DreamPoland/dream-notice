package cc.dreamcode.notice.minecraft;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.utilities.StringUtil;
import lombok.Getter;
import lombok.Setter;

public class NoticeImpl<R extends Notice<R>> extends Notice<R> {

    private final NoticeType noticeType;
    private final String noticeText;

    @Setter @Getter private int titleFadeIn = 10;
    @Setter @Getter private int titleStay = 20;
    @Setter @Getter private int titleFadeOut = 10;

    public NoticeImpl(NoticeType noticeType, String... noticeText) {
        this.noticeType = noticeType;

        if (noticeText.length == 1) {
            this.noticeText = noticeText[0];
            return;
        }

        this.noticeText = StringUtil.join(noticeText, NoticeImpl.lineSeparator());
    }

    @Override
    public String getRaw() {
        return this.noticeText;
    }

    @Override
    public Enum<?> getNoticeType() {
        return this.noticeType;
    }

    public static String lineSeparator() {
        return "%NEWLINE%";
    }
}
