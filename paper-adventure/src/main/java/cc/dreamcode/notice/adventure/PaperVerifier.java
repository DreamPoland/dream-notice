package cc.dreamcode.notice.adventure;

import cc.dreamcode.utilities.ClassUtil;

public class PaperVerifier {
    public static boolean verifyVersion() {

        if (!ClassUtil.hasClass("net.kyori.adventure.text.minimessage.ParsingException")) {
            return false;
        }

        return ClassUtil.hasClass("com.destroystokyo.paper.PaperConfig") ||
                ClassUtil.hasClass("io.papermc.paper.configuration.Configuration");
    }
}
