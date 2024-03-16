package cc.dreamcode.notice.minecraft.adventure.paper;

public class AdventurePaperVerifier {
    public static boolean verifyVersion() {

        if (!hasClass("net.kyori.adventure.text.minimessage.MiniMessage")) {
            return false;
        }

        return hasClass("com.destroystokyo.paper.PaperConfig") ||
                hasClass("io.papermc.paper.configuration.Configuration");
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
