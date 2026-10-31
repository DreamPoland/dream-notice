package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.adventure.AdventureNotice;
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
public class BungeeNotice extends AdventureNotice {

    public BungeeNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public BungeeNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }

    public BungeeNotice(@NonNull Notice<?> notice) {
        super(notice);
    }

    public static BungeeNotice of(@NonNull Notice<?> notice) {
        if (notice instanceof BungeeNotice bungeeNotice) {
            return bungeeNotice;
        }
        return new BungeeNotice(notice);
    }

    public static String lineSeparator() {
        return Notice.lineSeparator();
    }

    @Override
    public BungeeNotice with(@NonNull String placeholder, Object replace) {
        return new BungeeNotice(super.with(placeholder, replace));
    }

    @Override
    public BungeeNotice with(@NonNull Map<String, Object> replacements) {
        return new BungeeNotice(super.with(replacements));
    }

    @Override
    public BungeeNotice url(@NonNull String url) {
        return new BungeeNotice(super.url(url));
    }

    @Override
    public BungeeNotice url(@NonNull String text, @NonNull String url) {
        return new BungeeNotice(super.url(text, url));
    }

    @Override
    public BungeeNotice url(@NonNull String placeholder, @NonNull String text, @NonNull String url) {
        return new BungeeNotice(super.url(placeholder, text, url));
    }

    public static BungeeNotice chat(String... text) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(prepareText(text))
                .build();
    }

    public static BungeeNotice chat(List<String> text) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(prepareText(text))
                .build();
    }

    public static BungeeNotice actionbar(String text) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.ACTION_BAR)
                .text(text != null ? Collections.singletonList(text) : Collections.emptyList())
                .build();
    }

    public static BungeeNotice title(String title) {
        if (title != null && containsLineSeparator(title)) {
            String[] split = splitLineSeparator(title, 2);
            return BungeeNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .build();
        }
        return BungeeNotice.builder()
                .noticeType(NoticeType.TITLE)
                .title(title)
                .build();
    }

    public static BungeeNotice subtitle(String subtitle) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.SUBTITLE)
                .subtitle(subtitle)
                .build();
    }

    public static BungeeNotice title(String title, String subtitle) {
        if (title != null && containsLineSeparator(title) && (subtitle == null || subtitle.isEmpty())) {
            String[] split = splitLineSeparator(title, 2);
            return BungeeNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .build();
        }
        return BungeeNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .build();
    }

    public static BungeeNotice title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        if (title != null && containsLineSeparator(title) && (subtitle == null || subtitle.isEmpty())) {
            String[] split = splitLineSeparator(title, 2);
            return BungeeNotice.builder()
                    .noticeType(NoticeType.TITLE_SUBTITLE)
                    .title(split[0])
                    .subtitle(split.length > 1 ? split[1] : "")
                    .fadeIn(fadeIn)
                    .stay(stay)
                    .fadeOut(fadeOut)
                    .build();
        }
        return BungeeNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .build();
    }

    public static BungeeNotice bossBar(String title, float progress) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarProgress(progress)
                .build();
    }

    public static BungeeNotice bossBar(String title, String color, String overlay, float progress) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarColor(color)
                .bossBarOverlay(overlay)
                .bossBarProgress(progress)
                .build();
    }

    public static BungeeNotice book(String title, String author, List<String> pages) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? pages : Collections.emptyList())
                .build();
    }

    public static BungeeNotice book(String title, String author, String... pages) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? Arrays.asList(pages) : Collections.emptyList())
                .build();
    }

    public static BungeeNotice playerList(String header, String footer) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.PLAYER_LIST)
                .header(header)
                .footer(footer)
                .build();
    }

    public static BungeeNotice sound(String soundKey, float volume, float pitch) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static BungeeNotice sound(String soundKey, String category, float volume, float pitch) {
        return BungeeNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .soundCategory(category)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static BungeeNotice empty() {
        return BungeeNotice.builder()
                .noticeType(NoticeType.NONE)
                .build();
    }
}
