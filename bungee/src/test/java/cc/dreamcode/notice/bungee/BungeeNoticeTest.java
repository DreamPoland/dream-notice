package cc.dreamcode.notice.bungee;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeScheduler;
import cc.dreamcode.notice.bungee.adapter.BungeeAudienceAdapter;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BungeeNoticeTest {

    private BungeeNoticeProvider<BungeeConfig> provider;
    private BungeeConfig config;

    public static class BungeeConfig {
        public Notice<?> chatNotice = Notice.chat("Bungee hello {player}");
        public Notice<?> actionbarNotice = Notice.actionbar("Bungee action {player}");
    }

    @BeforeEach
    void setUp() {
        this.config = new BungeeConfig();
        this.provider = BungeeNoticeProvider.create(this.config, (NoticeScheduler) Runnable::run);
    }

    @Test
    void testBungeeNoticeAudienceWithConsole() {
        CommandSender console = mock(CommandSender.class);
        when(console.getName()).thenReturn("CONSOLE");

        BungeeNoticeAudience audience = new BungeeNoticeAudience(console);
        assertEquals("CONSOLE", audience.getName());
        assertNotNull(audience.asAdventureAudience());

        this.provider.builder()
                .target(console)
                .notice(c -> c.chatNotice)
                .with("player", "ConsoleAdmin")
                .send();

        verify(console, atLeastOnce()).sendMessage(any(BaseComponent[].class));
    }

    @Test
    void testBungeeNoticeAudienceWithPlayer() {
        ProxiedPlayer player = mock(ProxiedPlayer.class);
        when(player.getName()).thenReturn("BungeePlayer");
        when(player.getLocale()).thenReturn(Locale.ENGLISH);

        BungeeNoticeAudience audience = new BungeeNoticeAudience(player);
        assertEquals("BungeePlayer", audience.getName());
        assertEquals(Locale.ENGLISH, audience.getLocale());

        this.provider.builder()
                .player(player)
                .notice(c -> c.chatNotice)
                .with("player", "BungeePlayer")
                .send();

        verify(player, atLeastOnce()).sendMessage(any(BaseComponent[].class));
    }

    @Test
    void testBungeeAudienceAdapterDirectly() {
        CommandSender sender = mock(CommandSender.class);
        BungeeAudienceAdapter adapter = new BungeeAudienceAdapter(sender);

        adapter.sendMessage(Component.text("Hello Bungee"));
        verify(sender).sendMessage(any(BaseComponent[].class));

        ProxiedPlayer player = mock(ProxiedPlayer.class);
        BungeeAudienceAdapter playerAdapter = new BungeeAudienceAdapter(player);

        playerAdapter.sendActionBar(Component.text("ActionBar Bungee"));
        verify(player).sendMessage(any(ChatMessageType.class), any(BaseComponent[].class));

        playerAdapter.sendPlayerListHeaderAndFooter(Component.text("Top"), Component.text("Bottom"));
        verify(player).setTabHeader(any(BaseComponent[].class), any(BaseComponent[].class));
    }

    @Test
    void testCreateStaticFactory() {
        BungeeNoticeProvider<BungeeConfig> created = BungeeNoticeProvider.create(this.config, (NoticeScheduler) Runnable::run);
        assertNotNull(created);
        assertNotNull(created.getNoticeExecutor());
        assertSame(this.config, created.getMessageConfig());
    }
}
