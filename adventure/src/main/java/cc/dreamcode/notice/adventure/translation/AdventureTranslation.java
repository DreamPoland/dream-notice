package cc.dreamcode.notice.adventure.translation;

import cc.dreamcode.notice.translation.NoticeTranslation;
import lombok.Data;

import java.text.MessageFormat;
import java.util.Locale;

@Data
public class AdventureTranslation implements NoticeTranslation<MessageFormat> {

    private final Locale locale;
    private final MessageFormat translation;
}
