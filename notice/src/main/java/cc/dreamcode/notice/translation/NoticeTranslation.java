package cc.dreamcode.notice.translation;

import java.util.Locale;

public interface NoticeTranslation<T> {

    Locale getLocale();

    T getTranslation();
}
