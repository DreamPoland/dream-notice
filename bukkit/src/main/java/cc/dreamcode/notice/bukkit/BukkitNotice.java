package cc.dreamcode.notice.bukkit;

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
public class BukkitNotice extends AdventureNotice {

    public BukkitNotice(@NonNull NoticeType noticeType, TranslationRegistry<MessageFormat> translationRegistry) {
        super(noticeType, translationRegistry);
    }

    public BukkitNotice(@NonNull NoticeType noticeType, String... text) {
        super(noticeType, text);
    }

    public BukkitNotice(@NonNull Notice<?> notice) {
        super(notice);
    }

    public static BukkitNotice of(@NonNull Notice<?> notice) {
        if (notice instanceof BukkitNotice bukkitNotice) {
            return bukkitNotice;
        }
        return new BukkitNotice(notice);
    }

    @Override
    public BukkitNotice with(@NonNull String placeholder, Object replace) {
        return new BukkitNotice(super.with(placeholder, replace));
    }

    @Override
    public BukkitNotice with(@NonNull Map<String, Object> replacements) {
        return new BukkitNotice(super.with(replacements));
    }

    public static BukkitNotice chat(String... text) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(text != null ? Arrays.asList(text) : Collections.emptyList())
                .build();
    }

    public static BukkitNotice chat(List<String> text) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(text != null ? text : Collections.emptyList())
                .build();
    }

    public static BukkitNotice actionbar(String text) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.ACTION_BAR)
                .text(text != null ? Collections.singletonList(text) : Collections.emptyList())
                .build();
    }

    public static BukkitNotice title(String title) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.TITLE)
                .title(title)
                .build();
    }

    public static BukkitNotice subtitle(String subtitle) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.SUBTITLE)
                .subtitle(subtitle)
                .build();
    }

    public static BukkitNotice title(String title, String subtitle) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .build();
    }

    public static BukkitNotice title(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.TITLE_SUBTITLE)
                .title(title)
                .subtitle(subtitle)
                .fadeIn(fadeIn)
                .stay(stay)
                .fadeOut(fadeOut)
                .build();
    }

    public static BukkitNotice bossBar(String title, float progress) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarProgress(progress)
                .build();
    }

    public static BukkitNotice bossBar(String title, String color, String overlay, float progress) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.BOSS_BAR)
                .title(title)
                .bossBarColor(color)
                .bossBarOverlay(overlay)
                .bossBarProgress(progress)
                .build();
    }

    public static BukkitNotice book(String title, String author, List<String> pages) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? pages : Collections.emptyList())
                .build();
    }

    public static BukkitNotice book(String title, String author, String... pages) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.BOOK)
                .title(title)
                .bookAuthor(author)
                .pages(pages != null ? Arrays.asList(pages) : Collections.emptyList())
                .build();
    }

    public static BukkitNotice playerList(String header, String footer) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.PLAYER_LIST)
                .header(header)
                .footer(footer)
                .build();
    }

    public static BukkitNotice sound(String soundKey, float volume, float pitch) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static BukkitNotice sound(String soundKey, String category, float volume, float pitch) {
        return BukkitNotice.builder()
                .noticeType(NoticeType.SOUND)
                .soundKey(soundKey)
                .soundCategory(category)
                .volume(volume)
                .pitch(pitch)
                .build();
    }

    public static BukkitNotice empty() {
        return BukkitNotice.builder()
                .noticeType(NoticeType.NONE)
                .build();
    }
}
