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
        assertEquals(java.util.Collections.singletonList("Hello {player}!"), replaced.getText());
        assertEquals("John", replaced.getReplacements().get("player"));
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
    void testChatProcessorTreatsReplacementsAsPureLiteral() {
        AdventureNotice notice = AdventureNotice.chat("User: {player}");
        this.provider.builder()
                .player(this.audience)
                .notice(notice)
                .with("player", "Alice%NEWLINE%Bob")
                .send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience, Mockito.times(1)).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("User: Alice%NEWLINE%Bob", plain);
    }

    @Test
    void testChatProcessorReplacesNewlineInTemplate() {
        AdventureNotice notice = AdventureNotice.builder()
                .noticeType(NoticeType.CHAT)
                .text(java.util.Collections.singletonList("First%NEWLINE%Second"))
                .build();
        this.provider.builder()
                .player(this.audience)
                .notice(notice)
                .with("extra", "value")
                .send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("First\nSecond", plain);
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

    @Test
    void testUrlOnAdventureNotice() {
        AdventureNotice notice = AdventureNotice.chat("Click here!").url("https://dreamcode.cc");
        assertNotNull(notice);
        assertEquals(java.util.Collections.singletonList("<click:open_url:'https://dreamcode.cc'>Click here!</click>"), notice.getText());
    }

    @Test
    void testUrlRenderingComponentWithClickEvent() {
        AdventureNotice notice = AdventureNotice.chat("Click here!").url("https://dreamcode.cc");
        this.provider.builder().player(this.audience).notice(notice).send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        net.kyori.adventure.text.event.ClickEvent clickEvent = sent.clickEvent();
        if (clickEvent == null) {
            for (net.kyori.adventure.text.Component child : sent.children()) {
                if (child.clickEvent() != null) {
                    clickEvent = child.clickEvent();
                    break;
                }
            }
        }

        assertNotNull(clickEvent);
        assertEquals(net.kyori.adventure.text.event.ClickEvent.openUrl("https://dreamcode.cc"), clickEvent);
    }

    @Test
    void testChatProcessorWithOkaeriPluralPlaceholders() {
        AdventureNotice notice = AdventureNotice.chat("Masz {count} {oferta,oferty,ofert#count}!");
        this.provider.builder()
                .player(this.audience)
                .notice(notice)
                .with("count", 1)
                .send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("Masz 1 oferta!", plain);
    }

    @Test
    void testChatProcessorIgnoresUserInputTagsAndNewlines() {
        AdventureNotice notice = AdventureNotice.chat("<gold>[Chat]</gold> {player}: {message}");
        this.provider.builder()
                .player(this.audience)
                .notice(notice)
                .with("player", "Steve")
                .with("message", "siema {newline} <red>kolor</red> {oferta,oferty,ofert#0}")
                .send();

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("[Chat] Steve: siema {newline} <red>kolor</red> {oferta,oferty,ofert#0}", plain);
    }

    @Test
    void testAdminChatFormatWithNewlineInMessage() {
        AdventureNotice adminChatFormat = AdventureNotice.chat("&8[&4&lAC&8] &4{nick}: &c{message}");
        Notice<?> ready = adminChatFormat
                .with("nick", "Admin")
                .with("message", "%newline%");

        assertEquals(java.util.Collections.singletonList("&8[&4&lAC&8] &4{nick}: &c{message}"), ready.getText());
        assertEquals("Admin", ready.getReplacements().get("nick"));
        assertEquals("%newline%", ready.getReplacements().get("message"));

        ready.send(this.audience);

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("[AC] Admin: %newline%", plain);
    }

    @Test
    void testNoticeWithPluralPlaceholderSent() {
        AdventureNotice notice = AdventureNotice.chat("Masz {count} {oferta,oferty,ofert#count}!");
        Notice<?> ready = notice.with("count", 5);
        ready.send(this.audience);

        org.mockito.ArgumentCaptor<net.kyori.adventure.text.Component> captor = org.mockito.ArgumentCaptor.forClass(net.kyori.adventure.text.Component.class);
        Mockito.verify(this.audience).sendMessage(captor.capture());
        net.kyori.adventure.text.Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertEquals("Masz 5 ofert!", plain);
    }
}
