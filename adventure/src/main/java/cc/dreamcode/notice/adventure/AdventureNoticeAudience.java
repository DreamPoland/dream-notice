package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.NoticeAudience;
import lombok.Getter;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.identity.Identity;

import java.util.Locale;

@Getter
public class AdventureNoticeAudience implements NoticeAudience<Audience> {

    private final Audience audience;
    private final String name;
    private final Type type;

    public AdventureNoticeAudience(@NonNull Audience audience) {
        this.audience = audience;
        this.name = audience.get(Identity.NAME).orElse("Audience");
        this.type = Type.CLIENT;
    }

    public AdventureNoticeAudience(@NonNull Audience audience, String name, Type type) {
        this.audience = audience;
        this.name = name != null ? name : audience.get(Identity.NAME).orElse("Audience");
        this.type = type != null ? type : Type.CLIENT;
    }

    @Override
    public boolean hasPermission(@NonNull String permission) {
        return true;
    }

    @Override
    public Audience getHandler() {
        return this.audience;
    }

    @Override
    public Locale getLocale() {
        return this.audience.get(Identity.LOCALE).orElse(Locale.getDefault());
    }
}
