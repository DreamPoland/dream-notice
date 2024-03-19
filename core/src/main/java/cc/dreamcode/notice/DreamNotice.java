package cc.dreamcode.notice;

import cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;

import java.util.Locale;
import java.util.Map;

public abstract class DreamNotice<R extends DreamNotice<?>> {

    private Locale locale = Locale.forLanguageTag("pl");
    private String render = null;

    public abstract String getRaw();

    public abstract Enum<?> getNoticeType();

    public String getRender() {

        if (this.render == null) {
            return this.getRaw();
        }

        return this.render;
    }

    @SuppressWarnings("unchecked")
    public R setLocale(@NonNull Locale locale) {
        this.locale = locale;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull String from, @NonNull Object to) {

        this.render = StringUtil.replace(this.locale, this.getRender(), from, to);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull Map<String, Object> replaceMap) {

        this.render = StringUtil.replace(this.locale, this.getRender(), replaceMap);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R clearRender() {

        this.render = null;
        return (R) this;
    }
}
