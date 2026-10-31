package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.BossBarUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;

import java.util.Locale;
import java.util.Map;

public class AdventureBossBarProcessor implements NoticeProcessor<Audience> {

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "BOSS_BAR".equals(type) || "BOSSBAR".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        String title = notice.getTitle();
        if (title == null && notice.getText() != null && !notice.getText().isEmpty()) {
            title = notice.getText().get(0);
        }
        if (title == null) {
            return;
        }

        BossBar.Color color;
        try {
            color = BossBar.Color.valueOf(notice.getBossBarColor().toUpperCase());
        } catch (Exception e) {
            color = BossBar.Color.WHITE;
        }

        BossBar.Overlay overlay;
        try {
            overlay = BossBar.Overlay.valueOf(notice.getBossBarOverlay().toUpperCase());
        } catch (Exception e) {
            overlay = BossBar.Overlay.PROGRESS;
        }

        BossBarUtil.showBossBar(handler, title, notice.getBossBarProgress(), color, overlay, replacements);
    }
}
