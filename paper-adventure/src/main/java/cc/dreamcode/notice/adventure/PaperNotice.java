package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.notice.minecraft.NoticeType;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public class PaperNotice extends AdventureNotice<PaperNotice> implements PaperSender {
    public PaperNotice(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        super(noticeType, noticeText);

        if (!PaperVerifier.verifyVersion()) {
            throw new RuntimeException("AdventurePaper need Paper software (or his fork) and Mini-Message implementation. (1.18.2+)");
        }
    }

    public static PaperNotice of(@NonNull NoticeType noticeType, @NonNull String... noticeText) {
        return new PaperNotice(noticeType, noticeText);
    }

    public static PaperNotice chat(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.CHAT, noticeText);
    }

    public static PaperNotice actionBar(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.ACTION_BAR, noticeText);
    }

    public static PaperNotice title(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.TITLE, noticeText);
    }

    public static PaperNotice subtitle(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.TITLE, noticeText);
    }

    public static PaperNotice titleSubtitle(@NonNull String... noticeText) {
        return new PaperNotice(NoticeType.TITLE_SUBTITLE, noticeText);
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
            this.toSplitComponents().forEach(target::sendMessage);

            this.clearRender();
            return;
        }

        final NoticeType noticeType = (NoticeType) this.getNoticeType();
        switch (noticeType) {
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

                String[] split = this.getRender().split(NoticeImpl.lineSeparator());
                if (split.length == 0) {
                    throw new RuntimeException("Notice with TITLE_SUBTITLE need line-separator (" + NoticeImpl.lineSeparator() + ") to separate two messages.");
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
