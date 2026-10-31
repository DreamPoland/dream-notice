package cc.dreamcode.notice;

import cc.dreamcode.notice.translation.TranslationRegistry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Notice<T> {

    private static NoticeProvider<?> globalProvider;

    @Builder.Default
    private NoticeType noticeType = NoticeType.CHAT;

    @Builder.Default
    private List<String> text = new ArrayList<>();

    private String title;
    private String subtitle;

    private String header;
    private String footer;

    @Builder.Default
    private int fadeIn = 10;
    @Builder.Default
    private int stay = 70;
    @Builder.Default
    private int fadeOut = 20;

    @Builder.Default
    private String bossBarColor = "WHITE";
    @Builder.Default
    private String bossBarOverlay = "PROGRESS";
    @Builder.Default
    private float bossBarProgress = 1.0f;

    private String bookAuthor;
    @Builder.Default
    private List<String> pages = new ArrayList<>();

    private String soundKey;
    @Builder.Default
    private String soundCategory = "MASTER";
    @Builder.Default
    private float volume = 1.0f;
    @Builder.Default
    private float pitch = 1.0f;

    private TranslationRegistry<T> translationRegistry;

    public Notice(@NonNull NoticeType noticeType, TranslationRegistry<T> translationRegistry) {
        this.noticeType = noticeType;
        this.translationRegistry = translationRegistry;
    }

    public Notice(@NonNull NoticeType noticeType, String... text) {
        this.noticeType = noticeType;
        this.text = text != null ? Arrays.asList(text) : Collections.emptyList();
    }

    @SuppressWarnings("unchecked")
    public Notice(@NonNull Notice<?> notice) {
        this.noticeType = notice.getNoticeType();
        this.text = notice.getText() != null ? new ArrayList<>(notice.getText()) : new ArrayList<>();
        this.title = notice.getTitle();
        this.subtitle = notice.getSubtitle();
        this.header = notice.getHeader();
        this.footer = notice.getFooter();
        this.fadeIn = notice.getFadeIn();
        this.stay = notice.getStay();
        this.fadeOut = notice.getFadeOut();
        this.bossBarColor = notice.getBossBarColor();
        this.bossBarOverlay = notice.getBossBarOverlay();
        this.bossBarProgress = notice.getBossBarProgress();
        this.bookAuthor = notice.getBookAuthor();
        this.pages = notice.getPages() != null ? new ArrayList<>(notice.getPages()) : new ArrayList<>();
        this.soundKey = notice.getSoundKey();
        this.soundCategory = notice.getSoundCategory();
        this.volume = notice.getVolume();
        this.pitch = notice.getPitch();
        this.translationRegistry = (TranslationRegistry<T>) notice.getTranslationRegistry();
    }

    public static Notice<?> of(@NonNull Notice<?> notice) {
        return new Notice<>(notice);
    }

    public Notice<T> with(@NonNull String placeholder, Object replace) {
        Notice<T> notice = new Notice<>(this);
        notice.applyReplacement(placeholder, replace);
        return notice;
    }

    public Notice<T> with(@NonNull Map<String, Object> replacements) {
        Notice<T> notice = new Notice<>(this);
        replacements.forEach(notice::applyReplacement);
        return notice;
    }

    private void applyReplacement(@NonNull String placeholder, Object replace) {
        if (this.text != null && !this.text.isEmpty()) {
            this.text = this.text.stream()
                    .map(line -> replaceInText(line, placeholder, replace))
                    .collect(Collectors.toList());
        }
        if (this.title != null) {
            this.title = replaceInText(this.title, placeholder, replace);
        }
        if (this.subtitle != null) {
            this.subtitle = replaceInText(this.subtitle, placeholder, replace);
        }
        if (this.header != null) {
            this.header = replaceInText(this.header, placeholder, replace);
        }
        if (this.footer != null) {
            this.footer = replaceInText(this.footer, placeholder, replace);
        }
        if (this.bookAuthor != null) {
            this.bookAuthor = replaceInText(this.bookAuthor, placeholder, replace);
        }
        if (this.pages != null && !this.pages.isEmpty()) {
            this.pages = this.pages.stream()
                    .map(page -> replaceInText(page, placeholder, replace))
                    .collect(Collectors.toList());
        }
    }

    private static String replaceInText(String source, @NonNull String placeholder, Object replace) {
        if (source == null || source.isEmpty() || placeholder.isEmpty()) {
            return source;
        }

        String cleanPlaceholder = placeholder;
        if ((cleanPlaceholder.startsWith("{") && cleanPlaceholder.endsWith("}")) ||
            (cleanPlaceholder.startsWith("%") && cleanPlaceholder.endsWith("%"))) {
            cleanPlaceholder = cleanPlaceholder.substring(1, cleanPlaceholder.length() - 1);
        }

        String replacement = (replace != null) ? String.valueOf(replace) : "";
        String result = source
                .replace("{" + cleanPlaceholder + "}", replacement)
                .replace("%" + cleanPlaceholder + "%", replacement);

        if (result.contains(placeholder)) {
            result = result.replace(placeholder, replacement);
        }

        return result;
    }

    public static NoticeProvider<?> getGlobalProvider() {
        return globalProvider;
    }

    public static void setGlobalProvider(NoticeProvider<?> provider) {
        globalProvider = provider;
    }

    public static Notice<?> chat(String... text) {
        return Notice.builder()
                .noticeType(NoticeType.CHAT)
                .text(text != null ? Arrays.asList(text) : Collections.emptyList())
                .build();
    }

    public static Notice<?> chat(List<String> text) {
        return Notice.builder()
                .noticeType(NoticeType.CHAT)
                .text(text != null ? text : Collections.emptyList())
                .build();
    }

    public static Notice<?> actionbar(String text) {
        return Notice.builder()
                .noticeType(NoticeType.ACTION_BAR)
                .text(text != null ? Collections.singletonList(text) : Collections.emptyList())
                .build();
    }

    public static Notice<?> title(String title) {
        return Notice.builder()
                .noticeType(NoticeType.TITLE)
                .title(title)
                .build();
    }

    public static Notice<?> subtitle(String subtitle) {
        return Notice.builder()
                .noticeType(NoticeType.SUBTITLE)
                .subtitle(subtitle)
                .build();
    }

    public static Notice<?> title(String title, String subtitle) {
        return Notice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .build();
    }

    public static Notice<?> title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        return Notice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .build();
    }

    public static Notice<?> bossBar(String title, float progress) {
        return Notice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarProgress(progress)
                .build();
    }

    public static Notice<?> bossBar(String title, String color, String overlay, float progress) {
        return Notice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarColor(color)
                .bossBarOverlay(overlay)
                .bossBarProgress(progress)
                .build();
    }

    public static Notice<?> book(String title, String author, List<String> pages) {
        return Notice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? pages : Collections.emptyList())
                .build();
    }

    public static Notice<?> book(String title, String author, String... pages) {
        return Notice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? Arrays.asList(pages) : Collections.emptyList())
                .build();
    }

    public static Notice<?> playerList(String header, String footer) {
        return Notice.builder()
                .noticeType(NoticeType.PLAYER_LIST)
                .header(header)
                .footer(footer)
                .build();
    }

    public static Notice<?> sound(String soundKey, float volume, float pitch) {
        return Notice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static Notice<?> sound(String soundKey, String category, float volume, float pitch) {
        return Notice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .soundCategory(category)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static Notice<?> empty() {
        return Notice.builder()
                .noticeType(NoticeType.NONE)
                .build();
    }

    public void send(@NonNull Object target) {
        send(target, Collections.emptyMap());
    }

    public void send(@NonNull Object target, @NonNull String key, @NonNull Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        send(target, map);
    }

    public void send(@NonNull Object target, @NonNull Map<String, Object> replacements) {
        if (globalProvider == null) {
            throw new IllegalStateException("Global NoticeProvider is not set! Call Notice.setGlobalProvider(...) first.");
        }
        globalProvider.builder()
                .target(target)
                .notice(this)
                .with(replacements)
                .send();
    }

    public void send(@NonNull java.util.Collection<?> targets) {
        send(targets, Collections.emptyMap());
    }

    public void send(@NonNull java.util.Collection<?> targets, @NonNull String key, @NonNull Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        send(targets, map);
    }

    public void send(@NonNull java.util.Collection<?> targets, @NonNull Map<String, Object> replacements) {
        if (globalProvider == null) {
            throw new IllegalStateException("Global NoticeProvider is not set! Call Notice.setGlobalProvider(...) first.");
        }
        globalProvider.builder()
                .targets(targets)
                .notice(this)
                .with(replacements)
                .send();
    }

    public void sendAll() {
        sendAll(Collections.emptyMap());
    }

    public void sendAll(@NonNull String key, @NonNull Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        sendAll(map);
    }

    public void sendAll(@NonNull Map<String, Object> replacements) {
        if (globalProvider == null) {
            throw new IllegalStateException("Global NoticeProvider is not set! Call Notice.setGlobalProvider(...) first.");
        }
        globalProvider.builder()
                .all()
                .notice(this)
                .with(replacements)
                .send();
    }

    public static void sendNotice(@NonNull Object target, @NonNull Notice<?> notice) {
        notice.send(target);
    }

    public static void sendNotice(@NonNull Object target, @NonNull Notice<?> notice, @NonNull String key, @NonNull Object value) {
        notice.send(target, key, value);
    }

    public static void sendNotice(@NonNull Object target, @NonNull Notice<?> notice, @NonNull Map<String, Object> replacements) {
        notice.send(target, replacements);
    }

    public static void send(@NonNull Object target, @NonNull Notice<?> notice) {
        notice.send(target);
    }

    public static void send(@NonNull Object target, @NonNull Notice<?> notice, @NonNull String key, @NonNull Object value) {
        notice.send(target, key, value);
    }

    public static void send(@NonNull Object target, @NonNull Notice<?> notice, @NonNull Map<String, Object> replacements) {
        notice.send(target, replacements);
    }

    public static void send(@NonNull java.util.Collection<?> targets, @NonNull Notice<?> notice) {
        notice.send(targets);
    }

    public static void send(@NonNull java.util.Collection<?> targets, @NonNull Notice<?> notice, @NonNull String key, @NonNull Object value) {
        notice.send(targets, key, value);
    }

    public static void send(@NonNull java.util.Collection<?> targets, @NonNull Notice<?> notice, @NonNull Map<String, Object> replacements) {
        notice.send(targets, replacements);
    }

    public static void sendAll(@NonNull Notice<?> notice) {
        notice.sendAll();
    }

    public static void sendAll(@NonNull Notice<?> notice, @NonNull String key, @NonNull Object value) {
        notice.sendAll(key, value);
    }

    public static void sendAll(@NonNull Notice<?> notice, @NonNull Map<String, Object> replacements) {
        notice.sendAll(replacements);
    }
}
