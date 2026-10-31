package cc.dreamcode.notice.bungee.serializer;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bungee.BungeeNotice;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class BungeeNoticeSerdesTest {

    public static class ExampleConfig extends OkaeriConfig {
        public BungeeNotice notPlayer = BungeeNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
        public BungeeNotice simpleChat = BungeeNotice.chat("Simple message");
        public BungeeNotice actionbar = BungeeNotice.actionbar("Action message");
        public BungeeNotice title = BungeeNotice.title("Title", "Subtitle");
    }

    @Test
    void testSerializerSupport() {
        BungeeNoticeSerializer serializer = new BungeeNoticeSerializer();
        assertTrue(serializer.supports(BungeeNotice.class));
    }

    @Test
    void testConfigSerializationAndDeserialization() throws IOException {
        File tempFile = File.createTempFile("bungee-notice-test", ".yml");
        tempFile.deleteOnExit();

        ExampleConfig config = ConfigManager.create(ExampleConfig.class, (it) -> {
            it.withBindFile(tempFile);
            it.withConfigurer(new InMemoryConfigurer(), new BungeeNoticeSerdes());
            it.save();
            it.load();
        });

        assertNotNull(config.notPlayer);
        assertTrue(config.notPlayer instanceof BungeeNotice);
        assertEquals(NoticeType.CHAT, config.notPlayer.getNoticeType());
        assertEquals(Arrays.asList("&cTa komende mozna tylko wykonac z poziomu gracza."), config.notPlayer.getText());

        assertNotNull(config.simpleChat);
        assertTrue(config.simpleChat instanceof BungeeNotice);

        assertNotNull(config.actionbar);
        assertTrue(config.actionbar instanceof BungeeNotice);

        assertNotNull(config.title);
        assertTrue(config.title instanceof BungeeNotice);
    }
}
