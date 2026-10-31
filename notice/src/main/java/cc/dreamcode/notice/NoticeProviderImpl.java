package cc.dreamcode.notice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeProviderImpl<I> implements NoticeProvider<I> {

    private final I messageConfig;
    private NoticeExecutor<?, ?> noticeExecutor;
    private NoticeScheduler noticeScheduler;

    public NoticeProviderImpl(I messageConfig) {
        this.messageConfig = messageConfig;
        Notice.setGlobalProvider(this);
    }

    public NoticeProviderImpl(I messageConfig, NoticeExecutor<?, ?> noticeExecutor, NoticeScheduler noticeScheduler) {
        this.messageConfig = messageConfig;
        this.noticeExecutor = noticeExecutor;
        this.noticeScheduler = noticeScheduler;
        Notice.setGlobalProvider(this);
    }

    @Override
    public I getMessageConfig() {
        return this.messageConfig;
    }

    @Override
    public NoticeExecutor<?, ?> getNoticeExecutor() {
        return this.noticeExecutor;
    }

    @Override
    public NoticeScheduler getNoticeScheduler() {
        return this.noticeScheduler;
    }

    @Override
    public <T> NoticeBuilder<I, T> builder() {
        return new NoticeBuilder<>(this);
    }
}
