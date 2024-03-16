package cc.dreamcode.notice;

import lombok.NonNull;

import java.util.Collection;
import java.util.Map;

public interface DreamSender<T> {

    void send(@NonNull T target);

    void send(@NonNull T target, @NonNull Map<String, Object> mapReplacer);

    void send(@NonNull Collection<T> targets);

    void send(@NonNull Collection<T> targets, @NonNull Map<String, Object> mapReplacer);
}
