package cc.dreamcode.notice;

import cc.dreamcode.utilities.StringUtil;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.NonNull;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public abstract class Notice<R extends Notice<?>> {

    private Locale locale = Locale.forLanguageTag("pl");
    private PlaceholderContext placeholderContext = null;

    public abstract String getRaw();

    public abstract Enum<?> getNoticeType();

    public Optional<PlaceholderContext> getPlaceholderContext() {
        return Optional.ofNullable(this.placeholderContext);
    }

    public String getRender() {
        return this.getPlaceholderContext()
                .map(PlaceholderContext::apply)
                .orElse(this.getRaw());
    }

    @SuppressWarnings("unchecked")
    public R setLocale(@NonNull Locale locale) {
        this.locale = locale;
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull String from, @NonNull Object to) {

        if (this.placeholderContext == null) {
            final CompiledMessage compiledMessage = CompiledMessage.of(this.locale, this.getRaw());
            this.placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage);
        }

        this.placeholderContext.with(from, to);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R with(@NonNull Map<String, Object> replaceMap) {

        if (this.placeholderContext == null) {
            final CompiledMessage compiledMessage = CompiledMessage.of(this.locale, this.getRaw());
            this.placeholderContext = StringUtil.getPlaceholders().contextOf(compiledMessage);
        }

        this.placeholderContext.with(replaceMap);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R clearRender() {

        this.placeholderContext = null;
        return (R) this;
    }
}
