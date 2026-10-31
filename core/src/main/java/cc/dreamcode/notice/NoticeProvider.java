package cc.dreamcode.notice;

public interface NoticeProvider<I> {

    I getMessageConfig();

    NoticeExecutor<?, ?> getNoticeExecutor();

    NoticeScheduler getNoticeScheduler();

    <T> NoticeBuilder<I, T> builder();
}
