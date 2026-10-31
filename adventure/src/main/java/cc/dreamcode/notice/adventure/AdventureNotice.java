package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.translation.TranslationRegistry;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Data
@SuperBuilder(toBuilder = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NoArgsConstructor
public class AdventureNotice extends Notice<MessageFormat> {

    public AdventureNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public AdventureNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }

    public AdventureNotice(@NonNull Notice<?> notice) {
        super(notice);
    }

    public static AdventureNotice of(@NonNull Notice<?> notice) {
        if (notice instanceof AdventureNotice adventureNotice) {
            return adventureNotice;
        }
        return new AdventureNotice(notice);
    }

    public static String lineSeparator() {
        return Notice.lineSeparator();
    }

    @Override
    public AdventureNotice with(@NonNull String placeholder, Object replace) {
        return new AdventureNotice(super.with(placeholder, replace));
    }

    @Override
    public AdventureNotice with(@NonNull Map<String, Object> replacements) {
        return new AdventureNotice(super.with(replacements));
    }

    @Override
    public AdventureNotice url(@NonNull String url) {
        return new AdventureNotice(super.url(url));
    }

    @Override
    public AdventureNotice url(@NonNull String text, @NonNull String url) {
        return new AdventureNotice(super.url(text, url));
    }

    @Override
    public AdventureNotice url(@NonNull String placeholder, @NonNull String text, @NonNull String url) {
        return new AdventureNotice(super.url(placeholder, text, url));
    }

    public static AdventureNotice chat(String... text) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(prepareText(text))
                .build();
    }

    public static AdventureNotice chat(List<String> text) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(prepareText(text))
                .build();
    }

    public static AdventureNotice actionbar(String text) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.ACTION_BAR)
                .text(text != null ? Collections.singletonList(text) : Collections.emptyList())
                .build();
    }

    public static AdventureNotice title(String title) {
        if (title != null && containsLineSeparator(title)) {
            String[] split = splitLineSeparator(title, 2);
            return AdventureNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .build();
        }
        return AdventureNotice.builder()
                .noticeType(NoticeType.TITLE)
                .title(title)
                .build();
    }

    public static AdventureNotice subtitle(String subtitle) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.SUBTITLE)
                .subtitle(subtitle)
                .build();
    }

    public static AdventureNotice title(String title, String subtitle) {
        if (title != null && containsLineSeparator(title) && (subtitle == null || subtitle.isEmpty())) {
            String[] split = splitLineSeparator(title, 2);
            return AdventureNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .build();
        }
        return AdventureNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .build();
    }

    public static AdventureNotice title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title != null && containsLineSeparator(title) && (subtitle == null || subtitle.isEmpty())) {
            String[] split = splitLineSeparator(title, 2);
            return AdventureNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .fadeIn(fadeIn)
                    .stay(stay)
                    .fadeOut(fadeOut)
                    .build();
        }
        return AdventureNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .build();
    }

    public static AdventureNotice bossBar(String title, float progress) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarProgress(progress)
                .build();
    }

    public static AdventureNotice bossBar(String title, String color, String overlay, float progress) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarColor(color)
                .bossBarOverlay(overlay)
                .bossBarProgress(progress)
                .build();
    }

    public static AdventureNotice book(String title, String author, List<String> pages) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? pages : Collections.emptyList())
                .build();
    }

    public static AdventureNotice book(String title, String author, String... pages) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? Arrays.asList(pages) : Collections.emptyList())
                .build();
    }

    public static AdventureNotice playerList(String header, String footer) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.PLAYER_LIST)
                .header(header)
                .footer(footer)
                .build();
    }

    public static AdventureNotice sound(String soundKey, float volume, float pitch) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static AdventureNotice sound(String soundKey, String category, float volume, float pitch) {
        return AdventureNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .soundCategory(category)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static AdventureNotice empty() {
        return AdventureNotice.builder()
                .noticeType(NoticeType.NONE)
                .build();
    }
}
