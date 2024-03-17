package cc.dreamcode.notice.minecraft.adventure.paper;

import cc.dreamcode.utilities.ClassUtil;
import cc.dreamcode.utilities.bukkit.VersionUtil;

public class AdventurePaperVerifier {
    public static boolean verifyVersion() {

        if (!ClassUtil.hasClass("net.kyori.adventure.text.minimessage.ParsingException")) {
            return false;
        }

        return VersionUtil.isPaper();
    }
}
