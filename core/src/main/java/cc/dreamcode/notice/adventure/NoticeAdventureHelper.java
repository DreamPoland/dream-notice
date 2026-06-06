package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import net.kyori.adventure.text.Component;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import java.util.Arrays;
import java.util.List;

public final class NoticeAdventureHelper {

    public static Component toComponent(Notice<?> notice) {
        return AdventureUtil.component(notice.getNoticeText(), notice.getLocale(), notice.getPlaceholders());
    }

    public static Component toComponent(Notice<?> notice, boolean colorizePlaceholders) {
        return AdventureUtil.component(notice.getNoticeText(), notice.getLocale(), notice.getPlaceholders(), colorizePlaceholders);
    }

    public static List<Component> toComponents(Notice<?> notice) {
        return AdventureUtil.component(Arrays.asList(notice.getNoticeText().split(Notice.lineSeparator())), notice.getLocale(), notice.getPlaceholders());
    }

    public static List<Component> toComponents(Notice<?> notice, boolean colorizePlaceholders) {
        return AdventureUtil.component(Arrays.asList(notice.getNoticeText().split(Notice.lineSeparator())), notice.getLocale(), notice.getPlaceholders(), colorizePlaceholders);
    }

    public static String toLegacySection(Object component) {
        return AdventureUtil.toLegacySection((Component) component);
    }
}
