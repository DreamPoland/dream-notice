package cc.dreamcode.notice.minecraft.adventure.paper;

public class AdventurePaperVerifier {
    public static boolean verifyVersion() {
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
