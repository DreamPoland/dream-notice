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

    @Test
    void testWithMethodOnChatNotice() {
        Notice<?> notice = Notice.chat("Hello {player}!");
        Notice<?> replaced1 = notice.with("player", "Alice");
        assertEquals(Collections.singletonList("Hello Alice!"), replaced1.getText());

        Notice<?> noticeBraces = Notice.chat("Hello {player}!");
        Notice<?> replaced2 = noticeBraces.with("{player}", "Bob");
        assertEquals(Collections.singletonList("Hello Bob!"), replaced2.getText());

        Notice<?> noticePercent = Notice.chat("Hello %player%!");
        Notice<?> replaced3 = noticePercent.with("player", "Charlie");
        assertEquals(Collections.singletonList("Hello Charlie!"), replaced3.getText());

        Notice<?> multi = Notice.chat("User: {player}, Coins: {coins}");
        Notice<?> replacedMulti = multi.with("player", "Alice").with("coins", 100);
        assertEquals(Collections.singletonList("User: Alice, Coins: 100"), replacedMulti.getText());

        Notice<?> mapReplace = multi.with(Map.of("player", "Alice", "coins", 500));
        assertEquals(Collections.singletonList("User: Alice, Coins: 500"), mapReplace.getText());
    }

    @Test
    void testWithImmutability() {
        Notice<?> original = Notice.chat("Welcome {player}!");
        Notice<?> modified1 = original.with("player", "Alice");
        Notice<?> modified2 = original.with("player", "Bob");

        assertEquals(Collections.singletonList("Welcome {player}!"), original.getText());
        assertEquals(Collections.singletonList("Welcome Alice!"), modified1.getText());
        assertEquals(Collections.singletonList("Welcome Bob!"), modified2.getText());
    }

    @Test
    void testWithOnDifferentNoticeTypes() {
        Notice<?> title = Notice.title("Hello {player}", "Level: {level}")
                .with("player", "Alice")
                .with("level", 5);
        assertEquals("Hello Alice", title.getTitle());
        assertEquals("Level: 5", title.getSubtitle());

        Notice<?> book = Notice.book("Rules for {player}", "{author}", "Page 1: Welcome {player}", "Page 2")
                .with("player", "Alice")
                .with("author", "ServerAdmin");
        assertEquals("Rules for Alice", book.getTitle());
        assertEquals("ServerAdmin", book.getBookAuthor());
        assertEquals(Arrays.asList("Page 1: Welcome Alice", "Page 2"), book.getPages());

        Notice<?> playerList = Notice.playerList("Header {server}", "Footer {online}")
                .with("server", "DreamCode")
                .with("online", 42);
        assertEquals("Header DreamCode", playerList.getHeader());
        assertEquals("Footer 42", playerList.getFooter());
    }

    @Test
    void testWithAndSend() {
        NoticeAudience<Object> audience = Mockito.mock(NoticeAudience.class);
        Mockito.when(audience.getLocale()).thenReturn(Locale.ENGLISH);

        Notice<?> notice = Notice.chat("Welcome {player}!");
        Notice<?> prepared = notice.with("player", "Alice");
        prepared.send(audience);

        verify(this.executor).send(eq(prepared), eq(audience), eq(Locale.ENGLISH), eq(Collections.emptyMap()));
        assertEquals(Collections.singletonList("Welcome Alice!"), prepared.getText());
    }

    @Test
    void testLineSeparatorMethod() {
        assertEquals("%NEWLINE%", Notice.lineSeparator());
        assertEquals("%NEWLINE%", Notice.LINE_SEPARATOR);
    }

    @Test
    void testChatWithNewlinePlaceholder() {
        Notice<?> notice1 = Notice.chat("Line 1%NEWLINE%Line 2");
        assertEquals(Arrays.asList("Line 1", "Line 2"), notice1.getText());

        Notice<?> notice2 = Notice.chat("Line 1" + Notice.lineSeparator() + "Line 2");
        assertEquals(Arrays.asList("Line 1", "Line 2"), notice2.getText());

        Notice<?> noticeBraces = Notice.chat("Line 1{NEWLINE}Line 2");
        assertEquals(Arrays.asList("Line 1", "Line 2"), noticeBraces.getText());

        Notice<?> noticeActual = Notice.chat("Line 1\nLine 2");
        assertEquals(Arrays.asList("Line 1", "Line 2"), noticeActual.getText());

        Notice<?> noticeVarargs = Notice.chat("Line 1", "Line 2%NEWLINE%Line 3");
        assertEquals(Arrays.asList("Line 1", "Line 2", "Line 3"), noticeVarargs.getText());

        Notice<?> noticeList = Notice.chat(Arrays.asList("Line 1%NEWLINE%Line 2", "Line 3"));
        assertEquals(Arrays.asList("Line 1", "Line 2", "Line 3"), noticeList.getText());

        Notice<?> noticeConstructor = new Notice<>(NoticeType.CHAT, "A%NEWLINE%B");
        assertEquals(Arrays.asList("A", "B"), noticeConstructor.getText());
    }

    @Test
    void testTitleWithNewlinePlaceholder() {
        Notice<?> title1 = Notice.title("Title%NEWLINE%Subtitle");
        assertEquals(NoticeType.TITLE_SUBTITLE, title1.getNoticeType());
        assertEquals("Title", title1.getTitle());
        assertEquals("Subtitle", title1.getSubtitle());

        Notice<?> title2 = Notice.title("Title{NEWLINE}Subtitle");
        assertEquals(NoticeType.TITLE_SUBTITLE, title2.getNoticeType());
        assertEquals("Title", title2.getTitle());
        assertEquals("Subtitle", title2.getSubtitle());

        Notice<?> title3 = Notice.title("Title\nSubtitle");
        assertEquals(NoticeType.TITLE_SUBTITLE, title3.getNoticeType());
        assertEquals("Title", title3.getTitle());
        assertEquals("Subtitle", title3.getSubtitle());
    }

    @Test
    void testWithMethodWithNewlinePlaceholder() {
        Notice<?> notice = Notice.chat("User: {player}");
        Notice<?> replaced = notice.with("player", "Alice%NEWLINE%Bob");
        assertEquals(Collections.singletonList("User: Alice%NEWLINE%Bob"), replaced.getText());

        Notice<?> multiLine = Notice.chat("First%NEWLINE%Second").with("foo", "bar");
        assertEquals(Arrays.asList("First", "Second"), multiLine.getText());
    }

    @Test
    void testUrlMethod() {
        Notice<?> whole = Notice.chat("Click here!").url("https://dreamcode.cc");
        assertEquals(Collections.singletonList("<click:open_url:'https://dreamcode.cc'>Click here!</click>"), whole.getText());

        Notice<?> literalSubtext = Notice.chat("Visit: [CLICK]").url("[CLICK]", "https://dreamcode.cc");
        assertEquals(Collections.singletonList("Visit: <click:open_url:'https://dreamcode.cc'>[CLICK]</click>"), literalSubtext.getText());

        Notice<?> placeholder = Notice.chat("Visit: {website}").url("website", "https://dreamcode.cc");
        assertEquals(Collections.singletonList("Visit: <click:open_url:'https://dreamcode.cc'>https://dreamcode.cc</click>"), placeholder.getText());

        Notice<?> customText = Notice.chat("Visit: {website}").url("website", "DreamCode", "https://dreamcode.cc");
        assertEquals(Collections.singletonList("Visit: <click:open_url:'https://dreamcode.cc'>DreamCode</click>"), customText.getText());
    }

    @Test
    void testUrlImmutability() {
        Notice<?> original = Notice.chat("Click here!");
        Notice<?> modified = original.url("https://dreamcode.cc");

        assertEquals(Collections.singletonList("Click here!"), original.getText());
        assertEquals(Collections.singletonList("<click:open_url:'https://dreamcode.cc'>Click here!</click>"), modified.getText());
    }

    @Test
    void testOkaeriPluralPlaceholders() {
        Notice<?> notice1 = Notice.chat("Masz {count} {oferta,oferty,ofert#count}!");
        Notice<?> replaced1 = notice1.with("count", 1);
        assertEquals(Collections.singletonList("Masz 1 oferta!"), replaced1.getText());

        Notice<?> replaced2 = notice1.with("count", 2);
        assertEquals(Collections.singletonList("Masz 2 oferty!"), replaced2.getText());

        Notice<?> replaced5 = notice1.with("count", 5);
        assertEquals(Collections.singletonList("Masz 5 ofert!"), replaced5.getText());

        Notice<?> notice2 = Notice.chat("Masz {count} {oferta,oferty,ofert#0}!");
        Notice<?> replacedIndexed = notice2.with("0", 1);
        assertEquals(Collections.singletonList("Masz {count} oferta!"), replacedIndexed.getText());
    }

    @Test
    void testNoRecursivePlaceholderReplacement() {
        Notice<?> notice = Notice.chat("Msg: {message}");
        Notice<?> replaced = notice.with("message", "tekst {newline} <red>kolor</red> {other}");
        assertEquals(Collections.singletonList("Msg: tekst {newline} <red>kolor</red> {other}"), replaced.getText());

        Notice<?> multi = Notice.chat("A: {a}, B: {b}");
        Notice<?> replacedMulti = multi.with(Map.of("a", "{b}", "b", "wartość"));
        assertEquals(Collections.singletonList("A: {b}, B: wartość"), replacedMulti.getText());
    }
}
