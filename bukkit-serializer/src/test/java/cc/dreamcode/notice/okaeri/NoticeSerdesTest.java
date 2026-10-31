package cc.dreamcode.notice.okaeri;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import cc.dreamcode.notice.bukkit.serializer.BukkitNoticeSerdes;
import cc.dreamcode.notice.bukkit.serializer.BukkitNoticeSerializer;
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
        public BukkitNotice notPlayer = BukkitNotice.chat("&cTa komende mozna tylko wykonac z poziomu gracza.");
        public BukkitNotice simpleChat = BukkitNotice.chat("Simple message");
        public BukkitNotice multilineChat = BukkitNotice.chat("Line 1", "Line 2");
        public BukkitNotice actionbar = BukkitNotice.actionbar("Action message");
        public BukkitNotice title = BukkitNotice.title("Main Title", "Subtitle", 15, 60, 15);
        public BukkitNotice bossbar = BukkitNotice.bossBar("Boss title", "RED", "PROGRESS", 0.75f);
        public BukkitNotice book = BukkitNotice.book("Guide", "Author", "Page 1", "Page 2");
        public BukkitNotice sound = BukkitNotice.sound("entity.player.levelup", "MASTER", 1.0f, 1.5f);
    }

    @Test
    void testSerializerSupport() {
        BukkitNoticeSerializer serializer = new BukkitNoticeSerializer();
        assertTrue(serializer.supports(BukkitNotice.class));
    }

    @Test
    void testConfigSerializationAndDeserialization() throws IOException {
        File tempFile = File.createTempFile("bukkit-notice-test", ".yml");
        tempFile.deleteOnExit();

        ExampleConfig config = ConfigManager.create(ExampleConfig.class, (it) -> {
            it.withBindFile(tempFile);
            it.withConfigurer(new InMemoryConfigurer(), new BukkitNoticeSerdes());
            it.save();
            it.load();
        });

        assertNotNull(config.notPlayer);
        assertTrue(config.notPlayer instanceof BukkitNotice);
        assertEquals(NoticeType.CHAT, config.notPlayer.getNoticeType());
        assertEquals(Arrays.asList("&cTa komende mozna tylko wykonac z poziomu gracza."), config.notPlayer.getText());

        assertNotNull(config.simpleChat);
        assertTrue(config.simpleChat instanceof BukkitNotice);
        assertEquals(NoticeType.CHAT, config.simpleChat.getNoticeType());
        assertEquals(Arrays.asList("Simple message"), config.simpleChat.getText());

        assertNotNull(config.multilineChat);
        assertTrue(config.multilineChat instanceof BukkitNotice);
        assertEquals(Arrays.asList("Line 1", "Line 2"), config.multilineChat.getText());

        assertNotNull(config.actionbar);
        assertTrue(config.actionbar instanceof BukkitNotice);
        assertEquals(NoticeType.ACTION_BAR, config.actionbar.getNoticeType());
        assertEquals(Arrays.asList("Action message"), config.actionbar.getText());

        assertNotNull(config.title);
        assertTrue(config.title instanceof BukkitNotice);
        assertEquals(NoticeType.TITLE_SUBTITLE, config.title.getNoticeType());
        assertEquals("Main Title", config.title.getTitle());
        assertEquals("Subtitle", config.title.getSubtitle());

        assertNotNull(config.bossbar);
        assertTrue(config.bossbar instanceof BukkitNotice);
        assertEquals(NoticeType.BOSS_BAR, config.bossbar.getNoticeType());

        assertNotNull(config.book);
        assertTrue(config.book instanceof BukkitNotice);
        assertEquals(NoticeType.BOOK, config.book.getNoticeType());

        assertNotNull(config.sound);
        assertTrue(config.sound instanceof BukkitNotice);
        assertEquals(NoticeType.SOUND, config.sound.getNoticeType());
    }
}
