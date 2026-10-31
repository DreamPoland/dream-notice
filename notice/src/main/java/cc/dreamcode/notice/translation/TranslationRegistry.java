package cc.dreamcode.notice.translation;

import lombok.NonNull;

import java.util.Locale;
import java.util.Map;

public interface TranslationRegistry<T> {

    void defaultLocale(@NonNull Locale locale);

    boolean containsKey(@NonNull String key);

    boolean canTranslate(@NonNull String key, @NonNull Locale locale);

    void register(@NonNull String key, @NonNull Locale locale, @NonNull T translation);

    void registerAll(@NonNull Locale locale, @NonNull Map<String, T> translations);

    void unregister(@NonNull String key);
}
