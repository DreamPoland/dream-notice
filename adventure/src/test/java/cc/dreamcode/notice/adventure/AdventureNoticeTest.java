package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import net.kyori.adventure.audience.Audience;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureNoticeTest {

    private TestConfig config;
    private AdventureNoticeProvider<TestConfig> provider;
    private Audience audience;

    public static class TestConfig {
        public Notice<?> welcomeNotice = Notice.chat("Welcome {player} to the server!");
        public Notice<?> titleNotice = Notice.title("Title {player}", "Subtitle");
    }

    @BeforeEach
    void setUp() {
        this.config = new TestConfig();
        this.provider = AdventureNoticeProvider.create(this.config);
        this.audience = Mockito.mock(Audience.class);
    }

    @Test
    void testProviderBuilderWay() {
        assertDoesNotThrow(() -> {
            this.provider.builder()
                    .player(this.audience)
                    .notice(c -> c.welcomeNotice)
                    .with("player", "John")
                    .send();
        });
    }

    @Test
    void testDirectModelWay() {
        assertDoesNotThrow(() -> {
            Notice.chat("Hello {player}")
                    .send(this.audience, "player", "John");
        });
    }

    @Test
    void testCreateStaticFactory() {
        AdventureNoticeProvider<Object> created = AdventureNoticeProvider.create();
        assertNotNull(created);
        assertNotNull(created.getNoticeExecutor());

        AdventureNoticeProvider<TestConfig> createdWithConfig = AdventureNoticeProvider.create(this.config);
        assertNotNull(createdWithConfig);
        assertSame(this.config, createdWithConfig.getMessageConfig());
    }

    @Test
    void testWithOnAdventureNotice() {
        AdventureNotice notice = AdventureNotice.chat("Hello {player}!");
        AdventureNotice replaced = notice.with("player", "John");
        assertNotNull(replaced);
        assertEquals(java.util.Collections.singletonList("Hello John!"), replaced.getText());
    }

    @Test
    void testLineSeparatorOnAdventureNotice() {
        assertEquals("%NEWLINE%", AdventureNotice.lineSeparator());
    }

    @Test
    void testChatWithNewlinePlaceholder() {
        AdventureNotice notice = AdventureNotice.chat("Line 1%NEWLINE%Line 2");
        assertEquals(java.util.Arrays.asList("Line 1", "Line 2"), notice.getText());

        AdventureNotice noticeVarargs = AdventureNotice.chat("Line 1", "Line 2%NEWLINE%Line 3");
        assertEquals(java.util.Arrays.asList("Line 1", "Line 2", "Line 3"), noticeVarargs.getText());
    }

    @Test
    void testTitleWithNewlinePlaceholder() {
        AdventureNotice title = AdventureNotice.title("Main%NEWLINE%Sub");
        assertEquals(NoticeType.TITLE_SUBTITLE, title.getNoticeType());
        assertEquals("Main", title.getTitle());
        assertEquals("Sub", title.getSubtitle());
    }

    @Test
    void testChatProcessorSendsMultipleMessagesForNewline() {
        AdventureNotice notice = AdventureNotice.chat("First%NEWLINE%Second");
        this.provider.builder().player(this.audience).notice(notice).send();

        Mockito.verify(this.audience, Mockito.times(2)).sendMessage(Mockito.any(net.kyori.adventure.text.Component.class));
    }

    @Test
    void testTitleProcessorSplitsNewline() {
        AdventureNotice notice = AdventureNotice.title("Header%NEWLINE%Footer");
        this.provider.builder().player(this.audience).notice(notice).send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.title.Title> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.title.Title.class);
        Mockito.verify(this.audience).showTitle(captor.capture());
        net.kyori.adventure.title.Title sentTitle = captor.getValue();
        assertNotNull(sentTitle);
    }
}
