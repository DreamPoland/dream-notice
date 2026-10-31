package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.NoticeExecutor;
import cc.dreamcode.notice.NoticeProviderImpl;
import cc.dreamcode.notice.NoticeScheduler;

public class AdventureNoticeProvider<I> extends NoticeProviderImpl<I> {

    public AdventureNoticeProvider(I messageConfig) {
        super(messageConfig, new AdventureNoticeExecutor(), null);
    }

    public AdventureNoticeProvider(I messageConfig, NoticeScheduler noticeScheduler) {
        super(messageConfig, new AdventureNoticeExecutor(), noticeScheduler);
    }

    public AdventureNoticeProvider(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        super(messageConfig, noticeExecutor, noticeScheduler);
    }

    public static AdventureNoticeProvider<Object> create() {
        return new AdventureNoticeProvider<>(null);
    }

    public static <I> AdventureNoticeProvider<I> create(I messageConfig) {
        return new AdventureNoticeProvider<>(messageConfig);
    }

    public static <I> AdventureNoticeProvider<I> create(I messageConfig, NoticeScheduler noticeScheduler) {
        return new AdventureNoticeProvider<>(messageConfig, noticeScheduler);
    }

    public static <I> AdventureNoticeProvider<I> create(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        return new AdventureNoticeProvider<>(messageConfig, noticeExecutor, noticeScheduler);
    }
}
