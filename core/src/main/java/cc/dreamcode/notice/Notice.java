package cc.dreamcode.notice;

import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Data
public class Notice<R extends Notice<?>> {

    private final NoticeType noticeType;
    private final String noticeText;

    private Locale locale = Locale.forLanguageTag("pl");
    private int titleFadeIn = 10;
    private int titleStay = 20;
    private int titleFadeOut = 10;

    private final Map<String, Object> placeholders = new HashMap<>();

    public Notice(NoticeType noticeType, String... noticeText) {
        this.noticeType = noticeType;

        if (noticeText.length == 1) {
            this.noticeText = noticeText[0];
            return;
        }

        this.noticeText = StringUtil.join(noticeText, Notice.lineSeparator());
    }

    public boolean placeholdersExists() {
        return !this.placeholders.isEmpty();
    }

    public Map<String, Object> getPlaceholders() {
        return Collections.unmodifiableMap(this.placeholders);
    }

    @SuppressWarnings("unchecked")
    public R setLocale(Locale locale) {
        this.locale = locale;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull String from, @NonNull Object to) {
        this.placeholders.put(from, to);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull Map<String, Object> replaceMap) {
        this.placeholders.putAll(replaceMap);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R clearRender() {
        this.placeholders.clear();
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setTitleFadeIn(int titleFadeIn) {
        this.titleFadeIn = titleFadeIn;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setTitleStay(int titleStay) {
        this.titleStay = titleStay;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R setTitleFadeOut(int titleFadeOut) {
        this.titleFadeOut = titleFadeOut;
        return (R) this;
    }

    public static String lineSeparator() {
        return "%NEWLINE%";
    }
}
