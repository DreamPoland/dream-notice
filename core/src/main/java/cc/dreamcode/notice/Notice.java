package cc.dreamcode.notice;

import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
    private final List<Object> customComponents = new ArrayList<>();

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
        this.customComponents.clear();
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R withComponent(@NonNull Object component) {
        this.customComponents.add(component);
        return (R) this;
    }

    public List<Object> getCustomComponents() {
        return Collections.unmodifiableList(this.customComponents);
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

    public Object toComponent() {
        if (!isAdventureAvailable()) {
            return null;
        }
        try {
            return Class.forName("cc.dreamcode.notice.adventure.NoticeAdventureHelper")
                    .getMethod("toComponent", Notice.class)
                    .invoke(null, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Object toComponent(boolean colorizePlaceholders) {
        if (!isAdventureAvailable()) {
            return null;
        }
        try {
            return Class.forName("cc.dreamcode.notice.adventure.NoticeAdventureHelper")
                    .getMethod("toComponent", Notice.class, boolean.class)
                    .invoke(null, this, colorizePlaceholders);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<?> toComponents() {
        if (!isAdventureAvailable()) {
            return Collections.emptyList();
        }
        try {
            return (List<?>) Class.forName("cc.dreamcode.notice.adventure.NoticeAdventureHelper")
                    .getMethod("toComponents", Notice.class)
                    .invoke(null, this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public List<?> toComponents(boolean colorizePlaceholders) {
        if (!isAdventureAvailable()) {
            return Collections.emptyList();
        }
        try {
            return (List<?>) Class.forName("cc.dreamcode.notice.adventure.NoticeAdventureHelper")
                    .getMethod("toComponents", Notice.class, boolean.class)
                    .invoke(null, this, colorizePlaceholders);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isAdventureAvailable() {
        return cc.dreamcode.utilities.ClassUtil.hasClass("cc.dreamcode.utilities.adventure.AdventureUtil");
    }

    public static boolean isAudience(Object target) {
        if (!isAdventureAvailable()) {
            return false;
        }
        try {
            Class<?> audienceClass = Class.forName("net.kyori.adventure.audience.Audience");
            return audienceClass.isInstance(target);
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    public static String toLegacy(Object component) {
        if (isAdventureAvailable()) {
            try {
                return (String) Class.forName("cc.dreamcode.notice.adventure.NoticeAdventureHelper")
                        .getMethod("toLegacySection", Object.class)
                        .invoke(null, component);
            } catch (Exception e) {
                // Fallback
            }
        }
        return component.toString();
    }
}

