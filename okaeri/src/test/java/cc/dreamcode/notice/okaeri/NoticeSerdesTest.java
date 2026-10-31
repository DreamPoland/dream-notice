package cc.dreamcode.notice.okaeri;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.configurer.InMemoryConfigurer;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class NoticeSerdesTest {

    public static class ExampleConfig extends OkaeriConfig {
        public Notice<?> simpleChat = Notice.chat("Simple message");
        public Notice<?> multilineChat = Notice.chat("Line 1", "Line 2");
        public Notice<?> actionbar = Notice.actionbar("Action message");
        public Notice<?> title = Notice.title("Main Title", "Subtitle", 15, 60, 15);
        public Notice<?> bossbar = Notice.bossBar("Boss title", "RED", "PROGRESS", 0.75f);
        public Notice<?> book = Notice.book("Guide", "Author", "Page 1", "Page 2");
        public Notice<?> sound = Notice.sound("entity.player.levelup", "MASTER", 1.0f, 1.5f);
    }

    @Test
    void testSerializerSupport() {
        NoticeSerializer serializer = new NoticeSerializer();
        assertTrue(serializer.supports(Notice.class));
    }

    @Test
    void testConfigSerializationAndDeserialization() throws IOException {
        File tempFile = File.createTempFile("notice-test", ".yml");
        tempFile.deleteOnExit();

        ExampleConfig config = ConfigManager.create(ExampleConfig.class, (it) -> {
            it.withBindFile(tempFile);
            it.withConfigurer(new InMemoryConfigurer(), new NoticeSerdes());
            it.save();
            it.load();
        });

        assertNotNull(config.simpleChat);
        assertEquals(NoticeType.CHAT, config.simpleChat.getNoticeType());
        assertEquals(Arrays.asList("Simple message"), config.simpleChat.getText());

        assertNotNull(config.multilineChat);
        assertEquals(NoticeType.CHAT, config.multilineChat.getNoticeType());
        assertEquals(Arrays.asList("Line 1", "Line 2"), config.multilineChat.getText());

        assertNotNull(config.actionbar);
        assertEquals(NoticeType.ACTION_BAR, config.actionbar.getNoticeType());

        assertNotNull(config.title);
        assertEquals(NoticeType.TITLE_SUBTITLE, config.title.getNoticeType());
        assertEquals("Main Title", config.title.getTitle());
        assertEquals("Subtitle", config.title.getSubtitle());
        assertEquals(15, config.title.getFadeIn());
        assertEquals(60, config.title.getStay());
        assertEquals(15, config.title.getFadeOut());

        assertNotNull(config.bossbar);
        assertEquals(NoticeType.BOSS_BAR, config.bossbar.getNoticeType());
        assertEquals("Boss title", config.bossbar.getTitle());
        assertEquals("RED", config.bossbar.getBossBarColor());
        assertEquals(0.75f, config.bossbar.getBossBarProgress());

        assertNotNull(config.book);
        assertEquals(NoticeType.BOOK, config.book.getNoticeType());
        assertEquals("Guide", config.book.getTitle());
        assertEquals("Author", config.book.getBookAuthor());
        assertEquals(Arrays.asList("Page 1", "Page 2"), config.book.getPages());

        assertNotNull(config.sound);
        assertEquals(NoticeType.SOUND, config.sound.getNoticeType());
        assertEquals("entity.player.levelup", config.sound.getSoundKey());
        assertEquals(1.5f, config.sound.getPitch());
    }
}
