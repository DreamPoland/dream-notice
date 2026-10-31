package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.NoticeScheduler;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
public class BukkitNoticeScheduler implements NoticeScheduler {

    private final Plugin plugin;

    @Override
    public void sync(@NonNull Runnable runnable) {
        if (this.plugin == null || !this.plugin.isEnabled() || Bukkit.getServer() == null) {
            runnable.run();
            return;
        }
        try {
            if (Bukkit.isPrimaryThread()) {
                runnable.run();
            } else {
                Bukkit.getScheduler().runTask(this.plugin, runnable);
            }
        } catch (Throwable ignored) {
            runnable.run();
        }
    }

    @Override
    public void async(@NonNull Runnable runnable) {
        if (this.plugin == null || !this.plugin.isEnabled() || Bukkit.getServer() == null) {
            runnable.run();
            return;
        }
        try {
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, runnable);
        } catch (Throwable ignored) {
            runnable.run();
        }
    }
}
