package cc.dreamcode.notice.minecraft.adventure.bukkit;

import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.adventure.AdventureLegacy;
import cc.dreamcode.notice.minecraft.adventure.AdventureNotice;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class AdventureBukkitNotice extends AdventureNotice<AdventureBukkitNotice> implements AdventureBukkitSender {
    public AdventureBukkitNotice(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);
    }

    public static AdventureBukkitNotice of(@NonNull MinecraftNoticeType noticeType, @NonNull String... noticeText) {
        return new AdventureBukkitNotice(noticeType, noticeText);
    }

    @Override
    public void send(@NonNull CommandSender target) {
        final BukkitAudiences bukkitAudiences = AdventureBukkitNoticeProvider.getInstance().getBukkitAudiences();
        this.sendFormatted(target, bukkitAudiences.sender(target));
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

    private void sendFormatted(@NonNull CommandSender sender, @NonNull Audience target) {

        if (!(sender instanceof Player)) {
            this.toSplitComponents().forEach(target::sendMessage);

            this.clearRender();
            return;
        }

        final MinecraftNoticeType minecraftNoticeType = (MinecraftNoticeType) this.getNoticeType();
        switch (minecraftNoticeType) {
            case DO_NOT_SEND: {
                this.clearRender();
                break;
            }
            case CHAT: {
                this.toSplitComponents().forEach(target::sendMessage);

                this.clearRender();
                break;
            }
            case ACTION_BAR: {
                target.sendActionBar(this.toJoiningComponent());

                this.clearRender();
                break;
            }
            case TITLE: {
                final Component component = this.toJoiningComponent();
                final Component emptyComponent = AdventureLegacy.deserialize(" ");

                Title titleBuilder = Title.title(
                        component,
                        emptyComponent,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);

                this.clearRender();
                break;
            }
            case SUBTITLE: {
                final Component component = AdventureLegacy.deserialize(" ");
                final Component emptyComponent = this.toJoiningComponent();

                Title titleBuilder = Title.title(
                        component,
                        emptyComponent,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);

                this.clearRender();
                break;
            }
            case TITLE_SUBTITLE: {

                String[] split = this.getRender().split(MinecraftNotice.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + MinecraftNotice.lineSeparator() + ") to separate two messages.");
                }

                final Component title = AdventureLegacy.deserialize(split[0]);
                final Component subTitle = AdventureLegacy.deserialize(split[1]);

                Title titleBuilder = Title.title(
                        title,
                        subTitle,
                        Title.Times.times(
                                Duration.ofMillis(this.getTitleFadeIn() * 50L),
                                Duration.ofMillis(this.getTitleStay() * 50L),
                                Duration.ofMillis(this.getTitleFadeOut() * 50L)
                        )
                );

                target.showTitle(titleBuilder);

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
