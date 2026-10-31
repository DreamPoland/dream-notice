package cc.dreamcode.notice.adventure.serializer;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.adventure.AdventureNotice;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class AdventureNoticeSerdesTest {

    public static class ExampleConfig extends OkaeriConfig {
        public AdventureNotice simpleChat = AdventureNotice.chat("Simple message");
        public AdventureNotice actionbar = AdventureNotice.actionbar("Action message");
        public AdventureNotice title = AdventureNotice.title("Title", "Subtitle");
    }

    @Test
    void testSerializerSupport() {
        AdventureNoticeSerializer serializer = new AdventureNoticeSerializer();
        assertTrue(serializer.supports(AdventureNotice.class));
    }

    @Test
    void testConfigSerializationAndDeserialization() throws IOException {
        File tempFile = File.createTempFile("adventure-notice-test", ".yml");
        tempFile.deleteOnExit();

        ExampleConfig config = ConfigManager.create(ExampleConfig.class, (it) -> {
            it.withBindFile(tempFile);
            it.withConfigurer(new InMemoryConfigurer(), new AdventureNoticeSerdes());
            it.save();
            it.load();
        });

        assertNotNull(config.simpleChat);
        assertTrue(config.simpleChat instanceof AdventureNotice);
        assertEquals(NoticeType.CHAT, config.simpleChat.getNoticeType());
        assertEquals(Arrays.asList("Simple message"), config.simpleChat.getText());

        assertNotNull(config.actionbar);
        assertTrue(config.actionbar instanceof AdventureNotice);
        assertEquals(NoticeType.ACTION_BAR, config.actionbar.getNoticeType());

        assertNotNull(config.title);
        assertTrue(config.title instanceof AdventureNotice);
        assertEquals(NoticeType.TITLE_SUBTITLE, config.title.getNoticeType());
        assertEquals("Title", config.title.getTitle());
        assertEquals("Subtitle", config.title.getSubtitle());
    }
}
