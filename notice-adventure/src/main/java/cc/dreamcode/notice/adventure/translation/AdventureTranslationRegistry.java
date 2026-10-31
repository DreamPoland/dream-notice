package cc.dreamcode.notice.adventure.translation;

import cc.dreamcode.notice.translation.TranslationRegistry;
import cc.dreamcode.utilities.adventure.TranslationUtil;
import lombok.NonNull;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.translation.TranslationStore;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.Map;

public class AdventureTranslationRegistry implements TranslationRegistry<MessageFormat> {

    private final TranslationStore<MessageFormat> store;

    @SuppressWarnings("unchecked")
    public AdventureTranslationRegistry(@NonNull Key key, @NonNull Locale defaultLocale) {
        this.store = TranslationUtil.createAndRegisterComponentStore(key, defaultLocale);
    }

    @Override
    public void defaultLocale(@NonNull Locale locale) {
        this.store.defaultLocale(locale);
    }

    @Override
    public boolean containsKey(@NonNull String key) {
        return this.store.contains(key);
    }

    @Override
    public boolean canTranslate(@NonNull String key, @NonNull Locale locale) {
        return this.store.canTranslate(key, locale);
    }

    @Override
    public void register(@NonNull String key, @NonNull Locale locale, @NonNull MessageFormat translation) {
        this.store.register(key, locale, translation);
    }

    @Override
    public void registerAll(@NonNull Locale locale, @NonNull Map<String, MessageFormat> translations) {
        this.store.registerAll(locale, translations);
    }

    @Override
    public void unregister(@NonNull String key) {
        this.store.unregister(key);
    }
}
