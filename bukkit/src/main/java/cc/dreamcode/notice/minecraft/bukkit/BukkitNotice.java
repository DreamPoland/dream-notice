package cc.dreamcode.notice.minecraft.bukkit;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
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

public class BukkitNotice extends MinecraftNotice<BukkitNotice> implements BukkitSender {
    public BukkitNotice(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static BukkitNotice of(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        return new BukkitNotice(noticeType, noticeText);
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
            String[] split = this.getRender().split(MinecraftNotice.lineSeparator());
            Arrays.stream(split).forEach(text ->
                    target.sendMessage(StringColorUtil.fixColor(text)));
            return;
        }

        final Player player = (Player) target;
        final MinecraftNoticeType minecraftNoticeType = (MinecraftNoticeType) this.getNoticeType();
        switch (minecraftNoticeType) {
            case DO_NOT_SEND: {
                break;
            }
            case CHAT: {
                String[] split = this.getRender().split(MinecraftNotice.lineSeparator());
                Arrays.stream(split).forEach(text ->
                        player.sendMessage(StringColorUtil.fixColor(text)));
                break;
            }
            case ACTION_BAR: {
                ActionBar.sendActionBar(
                        player,
                        StringColorUtil.fixColor(this.getRender().replace(MinecraftNotice.lineSeparator(), ""))
                );
                break;
            }
            case TITLE: {
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        StringColorUtil.fixColor(this.getRender().replace(MinecraftNotice.lineSeparator(), "")),
                        ""
                );
                break;
            }
            case SUBTITLE: {
                Titles.sendTitle(
                        player,
                        this.getTitleFadeIn(),
                        this.getTitleStay(),
                        this.getTitleFadeOut(),
                        "",
                        StringColorUtil.fixColor(this.getRender().replace(MinecraftNotice.lineSeparator(), ""))
                );
                break;
            }
            case TITLE_SUBTITLE: {
                String[] split = this.getRender().split(MinecraftNotice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + MinecraftNotice.lineSeparator() + ") to separate two messages.");
                }

                final String title = StringColorUtil.fixColor(split[0]);
                final String subTitle = StringColorUtil.fixColor(split[1]);

                Titles.sendTitle(player, this.getTitleFadeIn(), this.getTitleStay(), this.getTitleFadeOut(), title, subTitle);
                break;
            }
            default:
                throw new RuntimeException("Cannot resolve notice-type. (" + this.getNoticeType() + ")");
        }
    }
}
