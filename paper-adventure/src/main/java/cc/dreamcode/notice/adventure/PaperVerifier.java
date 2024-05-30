package cc.dreamcode.notice.adventure;

import cc.dreamcode.utilities.ClassUtil;
import cc.dreamcode.utilities.bukkit.VersionUtil;

public class PaperVerifier {
    public static boolean verifyVersion() {

        if (!ClassUtil.hasClass("net.kyori.adventure.text.minimessage.ParsingException")) {
            return false;
        }

        return VersionUtil.isPaper();
    }
}
