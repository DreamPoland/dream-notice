package cc.dreamcode.notice.minecraft;

import cc.dreamcode.utilities.StringUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

@Data
@RequiredArgsConstructor
public abstract class MinecraftNotice<C> {

    private final MinecraftNoticeType type;
    private final String text;

    public MinecraftNotice(@NonNull MinecraftNoticeType type, @NonNull String... texts) {
        this.type = type;
        this.text = StringUtil.join(texts, lineSeparator());
    }

    public abstract void send(@NonNull C c);
    public abstract void send(@NonNull Collection<C> cCollection);
    public abstract void send(@NonNull C c, @NonNull Map<String, Object> mapReplacer);
    public abstract void send(@NonNull Collection<C> cCollection, @NonNull Map<String, Object> mapReplacer);
    public abstract void sendAll();
    public abstract void sendAll(@NonNull Map<String, Object> mapReplacer);

    public abstract void sendAllWithPermission(@NonNull String permission);
    public abstract void sendAllWithPermission(@NonNull String permission, @NonNull Map<String, Object> mapReplacer);

    public static String lineSeparator() {
        return "%NEWLINE%";
    }

}
