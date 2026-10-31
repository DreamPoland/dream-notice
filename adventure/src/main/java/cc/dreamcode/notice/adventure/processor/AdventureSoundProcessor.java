package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.AudienceUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.sound.Sound;

import java.util.Locale;
import java.util.Map;

public class AdventureSoundProcessor implements NoticeProcessor<Audience> {

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "SOUND".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        String soundKey = notice.getSoundKey();
        if (soundKey == null || soundKey.isEmpty()) {
            return;
        }

        Sound.Source source;
        try {
            source = Sound.Source.valueOf(notice.getSoundCategory().toUpperCase());
        } catch (Exception e) {
            source = Sound.Source.MASTER;
        }

        AudienceUtil.playSound(handler, soundKey, source, notice.getVolume(), notice.getPitch());
    }
}
