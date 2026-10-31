package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.NoticeScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.plugin.Plugin;

@RequiredArgsConstructor
public class BungeeNoticeScheduler implements NoticeScheduler {

    private final Plugin plugin;

    @Override
    public void sync(@NonNull Runnable runnable) {
        runnable.run();
    }

    @Override
    public void async(@NonNull Runnable runnable) {
        if (this.plugin == null) {
            runnable.run();
            return;
        }
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, runnable);
    }
}
