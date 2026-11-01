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
    private Function<I, ? extends Notice<?>> noticeFunction;
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
        if (notice != null && notice.getReplacements() != null && !notice.getReplacements().isEmpty()) {
            this.replacements.putAll(notice.getReplacements());
        }
        return this;
    }

    public NoticeBuilder<I, T> notice(Function<I, ? extends Notice<?>> noticeFunction) {
        this.noticeFunction = noticeFunction;
        return this;
    }

    public NoticeBuilder<I, T> with(@NonNull String key, Object value) {
        if ((key.startsWith("{") && key.endsWith("}")) || (key.startsWith("%") && key.endsWith("%"))) {
            key = key.substring(1, key.length() - 1);
        }
        this.replacements.put(key, value);
        return this;
    }

    public NoticeBuilder<I, T> with(@NonNull Map<String, Object> replacements) {
        for (Map.Entry<String, Object> entry : replacements.entrySet()) {
            String key = entry.getKey();
            if (key != null) {
                if ((key.startsWith("{") && key.endsWith("}")) || (key.startsWith("%") && key.endsWith("%"))) {
                    key = key.substring(1, key.length() - 1);
                }
                this.replacements.put(key, entry.getValue());
            }
        }
        return this;
    }

    public NoticeBuilder<I, T> url(@NonNull String url) {
        if (this.notice != null) {
            this.notice = this.notice.url(url);
        }
        return this;
    }

    public NoticeBuilder<I, T> url(@NonNull String text, @NonNull String url) {
        if (this.notice != null) {
            this.notice = this.notice.url(text, url);
        } else {
            this.replacements.put(text, Notice.formatUrlTag(url, url));
        }
        return this;
    }

    public NoticeBuilder<I, T> url(@NonNull String placeholder, @NonNull String text, @NonNull String url) {
        if (this.notice != null) {
            this.notice = this.notice.url(placeholder, text, url);
        } else {
            this.replacements.put(placeholder, Notice.formatUrlTag(text, url));
        }
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

        Map<String, Object> allReplacements = new HashMap<>();
        if (this.notice != null && this.notice.getReplacements() != null && !this.notice.getReplacements().isEmpty()) {
            allReplacements.putAll(this.notice.getReplacements());
        }
        allReplacements.putAll(this.replacements);

        Runnable sendTask = () -> {
            if (this.sendToAll) {
                if (executor instanceof NoticeAudienceResolver resolver) {
                    Collection<NoticeAudience<?>> audiences = resolver.getAllAudiences();
                    for (NoticeAudience aud : audiences) {
                        Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                        executor.send(this.notice, aud, targetLocale, allReplacements);
                    }
                }
                return;
            }

            for (Object target : this.targets) {
                if (target instanceof NoticeAudience aud) {
                    Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                    executor.send(this.notice, aud, targetLocale, allReplacements);
                } else if (executor instanceof NoticeAudienceResolver resolver) {
                    NoticeAudience aud = resolver.resolveAudience(target);
                    if (aud != null) {
                        Locale targetLocale = this.locale != null ? this.locale : aud.getLocale();
                        executor.send(this.notice, aud, targetLocale, allReplacements);
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
