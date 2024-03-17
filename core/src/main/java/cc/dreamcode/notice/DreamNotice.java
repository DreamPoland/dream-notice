package cc.dreamcode.notice;

import cc.dreamcode.utilities.StringUtil;
import lombok.NonNull;

import java.util.Map;

public abstract class DreamNotice<R extends DreamNotice<?>> {

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
    public R with(@NonNull String from, @NonNull Object to) {

        this.render = StringUtil.replace(this.getRender(), from, to);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull Map<String, Object> replaceMap) {

        this.render = StringUtil.replace(this.getRender(), replaceMap);
        return (R) this;
    }
}
