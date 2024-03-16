package cc.dreamcode.notice.minecraft;

import cc.dreamcode.notice.DreamNotice;
import cc.dreamcode.utilities.StringUtil;
import lombok.Getter;
import lombok.Setter;

public class MinecraftNotice<R extends DreamNotice<R>> extends DreamNotice<R> {

    private final MinecraftNoticeType noticeType;
    private final String noticeText;

    @Setter @Getter private int titleFadeIn = 10;
    @Setter @Getter private int titleStay = 20;
    @Setter @Getter private int titleFadeOut = 10;

    public MinecraftNotice(MinecraftNoticeType noticeType, String... noticeText) {
        this.noticeType = noticeType;

        if (noticeText.length == 1) {
            this.noticeText = noticeText[0];
            return;
        }

        this.noticeText = StringUtil.join(noticeText, MinecraftNotice.lineSeparator());
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
        return "\n";
    }
}
