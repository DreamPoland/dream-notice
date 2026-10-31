package cc.dreamcode.notice;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public class NoticeBuilder<I, T> {

    private final NoticeProvider<I> noticeProvider;
    private final List<Object> targets = new ArrayList<>();
    private Notice<?> notice;
    private Function<I, Notice<?>> noticeFunction;
    private final Map<String, Object> replacements = new HashMap<>();
    private Locale locale;
    private Boolean async;
    private boolean sendToAll;

    public NoticeBuilder(@NonNull NoticeProvider<I> noticeProvider) {
        this.noticeProvider = noticeProvider;
    }

    public NoticeBuilder<I, T> player(T player) {
        if (player != null) {
            this.targets.add(player);
        }
        return this;
    }

    public NoticeBuilder<I, T> audience(NoticeAudience<T> audience) {
        if (audience != null) {
            this.targets.add(audience);
        }
        return this;
    }

    public NoticeBuilder<I, T> target(Object target) {
        if (target != null) {
            this.targets.add(target);
        }
        return this;
    }

    public NoticeBuilder<I, T> targets(Collection<?> targets) {
        if (targets != null) {
            this.targets.addAll(targets);
        }
        return this;
    }

    public NoticeBuilder<I, T> all() {
        this.sendToAll = true;
        return this;
    }

    public NoticeBuilder<I, T> notice(Notice<?> notice) {
        this.notice = notice;
        return this;
    }

    public NoticeBuilder<I, T> notice(Function<I, Notice<?>> noticeFunction) {
        this.noticeFunction = noticeFunction;
        return this;
    }

    public NoticeBuilder<I, T> with(@NonNull String key, Object value) {
        this.replacements.put(key, value);
        return this;
    }

    public NoticeBuilder<I, T> with(@NonNull Map<String, Object> replacements) {
        this.replacements.putAll(replacements);
        return this;
    }

    public NoticeBuilder<I, T> locale(Locale locale) {
        this.locale = locale;
        return this;
    }

    public NoticeBuilder<I, T> sync() {
        this.async = false;
        return this;
    }

    public NoticeBuilder<I, T> async() {
        this.async = true;
        return this;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void send() {
        if (this.notice == null && this.noticeFunction != null) {
            I config = this.noticeProvider.getMessageConfig();
            if (config != null) {
                this.notice = this.noticeFunction.apply(config);
            }
        }

        if (this.notice == null || NoticeType.NONE.equals(this.notice.getNoticeType())) {
            return;
        }

        NoticeExecutor executor = this.noticeProvider.getNoticeExecutor();
        if (executor == null) {
            throw new IllegalStateException("NoticeExecutor is not set on NoticeProvider!");
        }

        NoticeScheduler scheduler = this.noticeProvider.getNoticeScheduler();

        Runnable sendTask = () -> {
            if (this.sendToAll) {
                if (executor instanceof NoticeAudienceResolver resolver) {
                    Collection<NoticeAudience<?>> audiences = resolver.getAllAudiences();
                    for (NoticeAudience aud : audiences) {
                        Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                        executor.send(this.notice, aud, targetLocale, this.replacements);
                    }
                }
                return;
            }

            for (Object target : this.targets) {
                if (target instanceof NoticeAudience aud) {
                    Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                    executor.send(this.notice, aud, targetLocale, this.replacements);
                } else if (executor instanceof NoticeAudienceResolver resolver) {
                    NoticeAudience aud = resolver.resolveAudience(target);
                    if (aud != null) {
                        Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                        executor.send(this.notice, aud, targetLocale, this.replacements);
                    }
                }
            }
        };

        boolean isAsync = Boolean.TRUE.equals(this.async);
        if (scheduler != null && isAsync) {
            scheduler.async(sendTask);
        } else if (scheduler != null) {
            scheduler.sync(sendTask);
        } else {
            sendTask.run();
        }
    }
}
