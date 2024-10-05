package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.utilities.bukkit.StringColorUtil;
import com.cryptomorin.xseries.messages.ActionBar;
import com.cryptomorin.xseries.messages.Titles;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

public class BukkitNotice extends Notice<BukkitNotice> implements BukkitSender {
    public BukkitNotice(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static BukkitNotice of(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        return new BukkitNotice(noticeType, noticeText);
    }

    public static BukkitNotice chat(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.CHAT, noticeText);
    }

    public static BukkitNotice actionBar(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.ACTION_BAR, noticeText);
    }

    public static BukkitNotice title(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.TITLE, noticeText);
    }

    public static BukkitNotice subtitle(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.SUBTITLE, noticeText);
    }

    public static BukkitNotice titleSubtitle(@NonNull String... noticeText) {
        return new BukkitNotice(NoticeType.TITLE_SUBTITLE, noticeText);
    }

    @Override
    public void send(@NonNull CommandSender target) {
        this.sendFormatted(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        this.sendFormatted(target);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        targets.forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.clearRender();
    }

    @Override
    public void sendBroadcast() {
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.sendFormatted(Bukkit.getConsoleSender());
        this.clearRender();
    }

    @Override
    public void sendBroadcast(@NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);
        Bukkit.getOnlinePlayers().forEach(this::sendFormatted);
        this.sendFormatted(Bukkit.getConsoleSender());
        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::sendFormatted);

        this.clearRender();
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer);

        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::sendFormatted);

        this.clearRender();
    }

    private void sendFormatted(@NonNull CommandSender target) {
        if (!(target instanceof Player)) {
            Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text ->
                    target.sendMessage(this.placeholdersExists()
                            ? StringColorUtil.fixColor(text, this.getPlaceholders())
                            : StringColorUtil.fixColor(text)));
            return;
        }

        final Player player = (Player) target;
        final NoticeType noticeType = this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                Arrays.stream(this.getNoticeText().split(Notice.lineSeparator())).forEach(text ->
                        target.sendMessage(this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text)));
                break;
            }
            case ACTION_BAR: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");
                ActionBar.sendActionBar(player, this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text));
                break;
            }
            case TITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text),
                        ""
                );
                break;
            }
            case SUBTITLE: {
                final String text = this.getNoticeText().replace(Notice.lineSeparator(), "");
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        "",
                        this.placeholdersExists()
                                ? StringColorUtil.fixColor(text, this.getPlaceholders())
                                : StringColorUtil.fixColor(text)
                );
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = this.getNoticeText().split(Notice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + Notice.lineSeparator() + ") to separate two messages.");
                }

                final String title;
                final String subTitle;

                if (this.placeholdersExists()) {
                    title = StringColorUtil.fixColor(split[0], this.getPlaceholders());
                    subTitle = StringColorUtil.fixColor(split[1], this.getPlaceholders());
                }
                else {
                    title = StringColorUtil.fixColor(split[0]);
                    subTitle = StringColorUtil.fixColor(split[1]);
                }

                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), title, subTitle);
                break;
            }
            default: {
                this.clearRender();

                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
            }
        }
    }
}
