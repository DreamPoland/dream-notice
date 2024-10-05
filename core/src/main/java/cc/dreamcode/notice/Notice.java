package cc.dreamcode.notice;

import lombok.NonNull;
import lombok.Setter;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class Notice {

    @Setter private Locale locale = Locale.forLanguageTag("pl");

    private final Map<String, Object> placeholders = new HashMap<>();

    public abstract String getRaw();
    public abstract Enum<?> getNoticeType();

    public boolean placeholdersExists() {
        return !this.placeholders.isEmpty();
    }

    public Map<String, Object> getPlaceholders() {
        return Collections.unmodifiableMap(this.placeholders);
    }

    public Notice with(@NonNull String from, @NonNull Object to) {
        this.placeholders.put(from, to);
        return this;
    }

    public Notice with(@NonNull Map<String, Object> replaceMap) {
        this.placeholders.putAll(replaceMap);
        return this;
    }

    public void clearRender() {
        this.placeholders.clear();
    }
}
