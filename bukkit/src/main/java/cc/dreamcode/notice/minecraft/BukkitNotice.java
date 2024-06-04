package cc.dreamcode.notice.minecraft;

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

public class BukkitNotice extends NoticeImpl<BukkitNotice> implements BukkitSender {
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
    }

    @Override
    public void send(@NonNull CommandSender target, @NonNull Map<String, Object> mapReplacer) {
        this.with(mapReplacer).send(target);
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets) {
        targets.forEach(this::send);
    }

    @Override
    public void send(@NonNull Collection<CommandSender> targets, @NonNull Map<String, Object> mapReplacer) {
        targets.forEach(target -> this.with(mapReplacer).send(target));
    }

    @Override
    public void sendAll() {
        Bukkit.getOnlinePlayers().forEach(this::send);
    }

    @Override
    public void sendAll(@NonNull Map<String, Object> mapReplacer) {
        Bukkit.getOnlinePlayers().forEach(target -> this.with(mapReplacer).send(target));
    }

    @Override
    public void sendPermitted(@NonNull String permission) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(this::send);
    }

    @Override
    public void sendPermitted(@NonNull String permission, @NonNull Map<String, Object> mapReplacer) {
        Bukkit.getOnlinePlayers()
                .stream()
                .filter(target -> target.hasPermission(permission))
                .forEach(target -> this.with(mapReplacer).send(target));
    }

    private void sendFormatted(@NonNull CommandSender target) {
        if (!(target instanceof Player)) {
            String[] split = this.getRender().split(NoticeImpl.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    target.sendMessage(StringColorUtil.fixColor(text)));

            this.clearRender();
            return;
        }

        final Player player = (Player) target;
        final NoticeType noticeType = (NoticeType) this.getNoticeType();
        switch (noticeType) {
            case DO_NOT_SEND: {
                this.clearRender();
                break;
            }
            case CHAT: {
                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(StringColorUtil.fixColor(text)));

                this.clearRender();
                break;
            }
            case ACTION_BAR: {
                ActionBar.sendActionBar(
                        player,
                        StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))
                );

                this.clearRender();
                break;
            }
            case TITLE: {
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), "")),
                        ""
                );

                this.clearRender();
                break;
            }
            case SUBTITLE: {
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        "",
                        StringColorUtil.fixColor(this.getRender().replace(NoticeImpl.lineSeparator(), ""))
                );

                this.clearRender();
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + NoticeImpl.lineSeparator() + ") to separate two messages.");
                }

                final String title = StringColorUtil.fixColor(split[0]);
                final String subTitle = StringColorUtil.fixColor(split[1]);

                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), title, subTitle);

                this.clearRender();
                break;
            }
            default: {
                this.clearRender();

                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
            }
        }
    }
}
