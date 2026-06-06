package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.utilities.adventure.AdventureUtil;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import java.time.Duration;
import java.util.List;

public final class BukkitAdventureHelper {

    @SuppressWarnings("unchecked")
    public static void send(BukkitNotice notice, CommandSender target, boolean colorizePlaceholders) {
        final Audience audience = (Audience) target;
        final NoticeType noticeType = notice.getNoticeType();
        switch (noticeType) {
            case CHAT: {
                ((List<Component>) (List<?>) notice.toComponents(colorizePlaceholders)).forEach(audience::sendMessage);
                break;
            }
            case ACTION_BAR: {
                audience.sendActionBar((Component) notice.toComponent(colorizePlaceholders));
                break;
            }
            case TITLE: {
                final Title.Times times = Title.Times.times(
                        Duration.ofMillis(notice.getTitleFadeIn() * 50L),
                        Duration.ofMillis(notice.getTitleStay() * 50L),
                        Duration.ofMillis(notice.getTitleFadeOut() * 50L)
                );
                final Title title = Title.title(
                        (Component) notice.toComponent(colorizePlaceholders),
                        Component.empty(),
                        times
                );
                audience.showTitle(title);
                break;
            }
            case SUBTITLE: {
                final Title.Times times = Title.Times.times(
                        Duration.ofMillis(notice.getTitleFadeIn() * 50L),
                        Duration.ofMillis(notice.getTitleStay() * 50L),
                        Duration.ofMillis(notice.getTitleFadeOut() * 50L)
                );
                final Title title = Title.title(
                        Component.empty(),
                        (Component) notice.toComponent(colorizePlaceholders),
                        times
                );
                audience.showTitle(title);
                break;
            }
            case TITLE_SUBTITLE: {
                final String[] split = notice.getNoticeText().split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + Notice.lineSeparator() + ") to separate two messages.");
                }
                final Component titleComp = AdventureUtil.component(split[0], notice.getLocale(), notice.getPlaceholders(), colorizePlaceholders);
                final Component subTitleComp = AdventureUtil.component(split[1], notice.getLocale(), notice.getPlaceholders(), colorizePlaceholders);
                final Title.Times times = Title.Times.times(
                        Duration.ofMillis(notice.getTitleFadeIn() * 50L),
                        Duration.ofMillis(notice.getTitleStay() * 50L),
                        Duration.ofMillis(notice.getTitleFadeOut() * 50L)
                );
                final Title title = Title.title(
                        titleComp,
                        subTitleComp,
                        times
                );
                audience.showTitle(title);
                break;
            }
        }
        ((List<Component>) (List<?>) notice.getCustomComponents()).forEach(audience::sendMessage);
    }
}
