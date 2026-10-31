package cc.dreamcode.notice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

public class NoticeTest {

    private SampleConfig sampleConfig;
    private NoticeExecutor<Object, ?> executor;
    private NoticeProviderImpl<SampleConfig> provider;

    public static class SampleConfig {
        public Notice<?> welcome = Notice.chat("Welcome {player}");
        public Notice<?> titleNotice = Notice.title("Header {player}", "Subheader");
        public Notice<?> bookNotice = Notice.book("Rules", "Admin", "Rule 1", "Rule 2");
    }

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        this.sampleConfig = new SampleConfig();
        this.executor = Mockito.mock(NoticeExecutor.class);
        this.provider = new NoticeProviderImpl<>(this.sampleConfig, this.executor, null);
    }

    @Test
    void testNoticeFactories() {
        Notice<?> chat = Notice.chat("Hello world");
        assertEquals(NoticeType.CHAT, chat.getNoticeType());
        assertEquals(Arrays.asList("Hello world"), chat.getText());

        Notice<?> title = Notice.title("Main", "Sub", 5, 20, 5);
        assertEquals(NoticeType.TITLE_SUBTITLE, title.getNoticeType());
        assertEquals("Main", title.getTitle());
        assertEquals("Sub", title.getSubtitle());

        Notice<?> bossbar = Notice.bossBar("Boss", "RED", "PROGRESS", 0.5f);
        assertEquals(NoticeType.BOSS_BAR, bossbar.getNoticeType());
        assertEquals("Boss", bossbar.getTitle());

        Notice<?> book = Notice.book("Rules", "Admin", "Rule 1", "Rule 2");
        assertEquals(NoticeType.BOOK, book.getNoticeType());
        assertEquals("Rules", book.getTitle());
        assertEquals("Admin", book.getBookAuthor());
    }

    @Test
    void testNoticeTypeFromName() {
        assertEquals(NoticeType.CHAT, NoticeType.fromName("chat"));
        assertEquals(NoticeType.ACTION_BAR, NoticeType.fromName("actionbar"));
        assertEquals(NoticeType.TITLE_SUBTITLE, NoticeType.fromName("title_subtitle"));
        assertEquals(NoticeType.BOSS_BAR, NoticeType.fromName("boss_bar"));
        assertEquals(NoticeType.BOOK, NoticeType.fromName("book"));
        assertEquals(NoticeType.PLAYER_LIST, NoticeType.fromName("player_list"));
        assertEquals(NoticeType.SOUND, NoticeType.fromName("sound"));
    }

    @Test
    void testProviderBuilderWay() {
        NoticeAudience<Object> audience = Mockito.mock(NoticeAudience.class);
        Mockito.when(audience.getLocale()).thenReturn(Locale.ENGLISH);

        this.provider.builder()
                .audience(audience)
                .notice(c -> c.welcome)
                .with("player", "Alice")
                .send();

        verify(this.executor).send(eq(this.sampleConfig.welcome), eq(audience), eq(Locale.ENGLISH), eq(Map.of("player", "Alice")));
    }

    @Test
    void testDirectModelWay() {
        NoticeAudience<Object> audience = Mockito.mock(NoticeAudience.class);
        Mockito.when(audience.getLocale()).thenReturn(Locale.ENGLISH);

        Notice<?> notice = Notice.actionbar("ActionBar message");
        notice.send(audience, "key", "val");

        verify(this.executor).send(eq(notice), eq(audience), eq(Locale.ENGLISH), eq(Map.of("key", "val")));
    }

    @Test
    void testSendCollectionAndSendAll() {
        NoticeAudience<Object> aud1 = Mockito.mock(NoticeAudience.class);
        Mockito.when(aud1.getLocale()).thenReturn(Locale.ENGLISH);

        Notice<?> notice = Notice.chat("Group message");
        notice.send(Collections.singletonList(aud1), "group", "admins");

        verify(this.executor).send(eq(notice), eq(aud1), eq(Locale.ENGLISH), eq(Map.of("group", "admins")));
    }
}
