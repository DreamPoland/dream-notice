package cc.dreamcode.notice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public abstract class Notice<C> {

    private final NoticeType type;
    private final String text;
    private int duration = 70;

    public Notice(@NonNull NoticeType type, @NonNull String... texts) {
        this.type = type;

        final StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(texts).forEach(text ->
                stringBuilder.append(text).append(lineSeparator()));
        this.text = stringBuilder.toString();
    }

    public abstract void send(@NonNull C c);
    public abstract void send(@NonNull Collection<C> cCollection);
    public abstract void send(@NonNull C c, @NonNull Map<String, Object> mapReplacer);
    public abstract void send(@NonNull Collection<C> cCollection, @NonNull Map<String, Object> mapReplacer);

    public static String lineSeparator() {
        return "%NEWLINE%";
    }

}
