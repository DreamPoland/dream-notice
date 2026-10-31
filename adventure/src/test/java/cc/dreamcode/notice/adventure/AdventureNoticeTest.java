package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
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
}
