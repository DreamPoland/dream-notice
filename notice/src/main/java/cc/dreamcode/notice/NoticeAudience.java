package cc.dreamcode.notice;

import lombok.NonNull;
import java.util.Locale;

public interface NoticeAudience<T> {

    Type getType();

    String getName();

    boolean hasPermission(@NonNull String permission);

    T getHandler();

    default Locale getLocale() {
        return Locale.getDefault();
    }

    enum Type {
        CONSOLE, CLIENT, ALL
    }
}
