package cc.dreamcode.notice.bukkit;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.bukkit.adapter.SpigotAudienceAdapter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BukkitNoticeTest {

    private Plugin plugin;
    private BukkitNoticeProvider<BukkitConfig> provider;
    private BukkitConfig config;

    public static class BukkitConfig {
        public Notice<?> chatNotice = Notice.chat("Hello {player}");
        public Notice<?> actionbarNotice = Notice.actionbar("Action {player}");
        public Notice<?> titleNotice = Notice.title("Title {player}", "Subtitle");
    }

    @BeforeEach
    void setUp() {
        this.plugin = mock(Plugin.class);
        when(this.plugin.isEnabled()).thenReturn(true);
        this.config = new BukkitConfig();
        this.provider = BukkitNoticeProvider.create(this.config, this.plugin);
    }

    @Test
    void testBukkitNoticeAudienceWithConsoleSender() {
        ConsoleCommandSender console = mock(ConsoleCommandSender.class);
        when(console.getName()).thenReturn("CONSOLE");

        BukkitNoticeAudience audience = new BukkitNoticeAudience(console);
        assertEquals("CONSOLE", audience.getName());
        assertNotNull(audience.asAdventureAudience());

        this.provider.builder()
                .target(console)
                .notice(c -> c.chatNotice)
                .with("player", "ConsoleAdmin")
                .send();

        verify(console, atLeastOnce()).sendMessage(any(Component.class));
    }

    @Test
    void testBukkitNoticeAudienceWithPlayerSender() {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("TestPlayer");
        when(player.locale()).thenReturn(Locale.forLanguageTag("pl-PL"));

        BukkitNoticeAudience audience = new BukkitNoticeAudience(player);
        assertEquals("TestPlayer", audience.getName());
        assertEquals("pl", audience.getLocale().getLanguage());

        this.provider.builder()
                .player(player)
                .notice(c -> c.titleNotice)
                .with("player", "TestPlayer")
                .send();

        verify(player, atLeastOnce()).showTitle(any(Title.class));
    }

    @Test
    void testSpigotAudienceAdapterDirectly() {
        CommandSender sender = mock(CommandSender.class);
        SpigotAudienceAdapter adapter = new SpigotAudienceAdapter(sender);

        adapter.sendMessage(Component.text("Test message"));
        verify(sender).sendMessage("Test message");

        Player player = mock(Player.class);
        SpigotAudienceAdapter playerAdapter = new SpigotAudienceAdapter(player);

        playerAdapter.showTitle(Title.title(
                Component.text("Main"),
                Component.text("Sub"),
                Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(2), Duration.ofSeconds(1))
        ));
        verify(player).sendTitle(eq("Main"), eq("Sub"), eq(20), eq(40), eq(20));

        playerAdapter.clearTitle();
        verify(player).sendTitle(eq(""), eq(""), eq(0), eq(0), eq(0));

        playerAdapter.resetTitle();
        verify(player).resetTitle();

        playerAdapter.sendPlayerListHeaderAndFooter(Component.text("Header"), Component.text("Footer"));
        verify(player).setPlayerListHeaderFooter("Header", "Footer");
    }

    @Test
    void testCreateStaticFactory() {
        BukkitNoticeProvider<Object> created = BukkitNoticeProvider.create(this.plugin);
        assertNotNull(created);
        assertNotNull(created.getNoticeExecutor());
        assertNotNull(created.getNoticeScheduler());

        BukkitNoticeProvider<BukkitConfig> createdWithConfig = BukkitNoticeProvider.create(this.config, this.plugin);
        assertNotNull(createdWithConfig);
        assertSame(this.config, createdWithConfig.getMessageConfig());
    }

    @Test
    void testWithOnBukkitNotice() {
        BukkitNotice notice = BukkitNotice.chat("Hello {player}!");
        BukkitNotice replaced = notice.with("player", "Alice");
        assertNotNull(replaced);
        assertEquals(java.util.Collections.singletonList("Hello {player}!"), replaced.getText());
        assertEquals("Alice", replaced.getReplacements().get("player"));
    }

    @Test
    void testLineSeparatorOnBukkitNotice() {
        assertEquals("%NEWLINE%", BukkitNotice.lineSeparator());
    }

    @Test
    void testChatWithNewlinePlaceholder() {
        BukkitNotice notice = BukkitNotice.chat("Line 1%NEWLINE%Line 2");
        assertEquals(java.util.Arrays.asList("Line 1", "Line 2"), notice.getText());
    }

    @Test
    void testTitleWithNewlinePlaceholder() {
        BukkitNotice title = BukkitNotice.title("Title%NEWLINE%Subtitle");
        assertEquals(cc.dreamcode.notice.NoticeType.TITLE_SUBTITLE, title.getNoticeType());
        assertEquals("Title", title.getTitle());
        assertEquals("Subtitle", title.getSubtitle());
    }

    @Test
    void testUrlOnBukkitNotice() {
        BukkitNotice notice = BukkitNotice.chat("Click here!").url("https://dreamcode.cc");
        assertNotNull(notice);
        assertEquals(java.util.Collections.singletonList("<click:open_url:'https://dreamcode.cc'>Click here!</click>"), notice.getText());
    }

    @Test
    void testBukkitNoticeSendWithReplacements() {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("Ravis96");

        BukkitNotice notice = BukkitNotice.chat("[+] Gracz {nick} dolaczyl na serwer ({sector-name})");
        notice.with("nick", "Ravis96")
                .with("sector-name", "s1")
                .send(player);

        org.mockito.ArgumentCaptor<Component> captor = org.mockito.ArgumentCaptor.forClass(Component.class);
        verify(player).sendMessage(captor.capture());
        Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertTrue(plain.contains("Ravis96"), "Expected 'Ravis96' in: " + plain);
        assertTrue(plain.contains("s1"), "Expected 's1' in: " + plain);
    }

    @Test
    void testBukkitNoticeMarketBroadcast() {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("Ravis96");

        BukkitNotice notice = BukkitNotice.chat("&8[&2&l!&8] &aNa bazarku znajduje sie aktualnie {offers} {oferta,oferty,ofert#offers}. Sprawdz je pod /bazar.");
        notice.with("offers", 1).send(player);

        org.mockito.ArgumentCaptor<Component> captor = org.mockito.ArgumentCaptor.forClass(Component.class);
        verify(player).sendMessage(captor.capture());
        Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertTrue(plain.contains("1 oferta"), "Expected '1 oferta' in: " + plain);
    }

    @Test
    void testBukkitNoticeCollectionSendWithMap() {
        Player player = mock(Player.class);
        when(player.getName()).thenReturn("Ravis96");

        BukkitNotice notice = BukkitNotice.chat("[+] Gracz {nick} dolaczyl na serwer ({sector-name})");
        notice.send(java.util.Collections.singletonList(player), Map.of("nick", "Ravis96", "sector-name", "s1"));

        org.mockito.ArgumentCaptor<Component> captor = org.mockito.ArgumentCaptor.forClass(Component.class);
        verify(player).sendMessage(captor.capture());
        Component sent = captor.getValue();
        assertNotNull(sent);

        String plain = net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer.plainText().serialize(sent);
        assertTrue(plain.contains("Ravis96"), "Expected 'Ravis96' in: " + plain);
        assertTrue(plain.contains("s1"), "Expected 's1' in: " + plain);
    }
}
