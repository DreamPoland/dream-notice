package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeAudienceResolver;
import cc.dreamcode.notice.NoticeExecutor;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.adventure.processor.AdventureActionBarProcessor;
import cc.dreamcode.notice.adventure.processor.AdventureBookProcessor;
import cc.dreamcode.notice.adventure.processor.AdventureBossBarProcessor;
import cc.dreamcode.notice.adventure.processor.AdventureChatProcessor;
import cc.dreamcode.notice.adventure.processor.AdventurePlayerListProcessor;
import cc.dreamcode.notice.adventure.processor.AdventureSoundProcessor;
import cc.dreamcode.notice.adventure.processor.AdventureTitleProcessor;
import cc.dreamcode.notice.processor.NoticeProcessor;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AdventureNoticeExecutor implements NoticeExecutor<Audience, AdventureNoticeExecutor>, NoticeAudienceResolver {

    private final List<NoticeProcessor<Audience>> processors = new ArrayList<>();
    private final AdventureSoundProcessor soundProcessor = new AdventureSoundProcessor();

    public AdventureNoticeExecutor() {
        this.processors.add(new AdventureChatProcessor());
        this.processors.add(new AdventureActionBarProcessor());
        this.processors.add(new AdventureTitleProcessor());
        this.processors.add(new AdventureBossBarProcessor());
        this.processors.add(new AdventureBookProcessor());
        this.processors.add(new AdventurePlayerListProcessor());
        this.processors.add(new AdventureSoundProcessor());
    }

    public AdventureNoticeExecutor registerProcessor(@NonNull NoticeProcessor<Audience> processor) {
        this.processors.add(processor);
        return this;
    }

    @Override
    public void send(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> noticeAudience, @NonNull Locale fallbackLocale, @NonNull Map<String, Object> replacements) {
        Audience handler = noticeAudience.getHandler();
        if (handler == null) {
            return;
        }

        NoticeType type = notice.getNoticeType();
        if (type == null || NoticeType.NONE.equals(type)) {
            return;
        }

        Locale targetLocale = resolveLocale(noticeAudience, handler, fallbackLocale);

        boolean processed = false;
        for (NoticeProcessor<Audience> processor : this.processors) {
            if (processor.supports(type)) {
                processor.process(notice, noticeAudience, targetLocale, replacements);
                processed = true;
            }
        }

        if (!processed) {
            new AdventureChatProcessor().process(notice, noticeAudience, targetLocale, replacements);
        }

        if (!"SOUND".equalsIgnoreCase(type.getName()) && notice.getSoundKey() != null && !notice.getSoundKey().isEmpty()) {
            this.soundProcessor.process(notice, noticeAudience, targetLocale, replacements);
        }
    }

    private Locale resolveLocale(NoticeAudience<Audience> noticeAudience, Audience handler, Locale fallbackLocale) {
        if (noticeAudience != null && noticeAudience.getLocale() != null) {
            return noticeAudience.getLocale();
        }
        return handler.get(Identity.LOCALE).orElse(fallbackLocale != null ? fallbackLocale : Locale.getDefault());
    }

    @Override
    public NoticeAudience<?> resolveAudience(@NonNull Object target) {
        if (target instanceof NoticeAudience<?> noticeAudience) {
            return noticeAudience;
        }
        if (target instanceof Audience audience) {
            return new AdventureNoticeAudience(audience);
        }
        return null;
    }

    @Override
    public Collection<NoticeAudience<?>> getAllAudiences() {
        return Collections.emptyList();
    }
}
