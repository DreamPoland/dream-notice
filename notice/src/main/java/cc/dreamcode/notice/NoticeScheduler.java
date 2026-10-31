package cc.dreamcode.notice;

import lombok.NonNull;

public interface NoticeScheduler {

    default void sync(@NonNull Runnable runnable) {
        runnable.run();
    }

    void async(@NonNull Runnable runnable);
}