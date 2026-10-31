package cc.dreamcode.notice.adventure.processor;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeAudience;
import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.processor.NoticeProcessor;
import cc.dreamcode.utilities.adventure.BookUtil;
import lombok.NonNull;
import net.kyori.adventure.audience.Audience;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class AdventureBookProcessor implements NoticeProcessor<Audience> {

    @Override
    public boolean supports(@NonNull NoticeType noticeType) {
        String type = noticeType.getName().toUpperCase();
        return "BOOK".equals(type);
    }

    @Override
    public void process(@NonNull Notice<?> notice, @NonNull NoticeAudience<Audience> audience, @NonNull Locale locale, @NonNull Map<String, Object> replacements) {
        Audience handler = audience.getHandler();
        if (handler == null) {
            return;
        }

        String title = notice.getTitle() != null ? notice.getTitle() : "Book";
        String author = notice.getBookAuthor() != null ? notice.getBookAuthor() : "Server";
        List<String> pages = notice.getPages() != null && !notice.getPages().isEmpty() ? notice.getPages() : notice.getText();

        if (pages == null || pages.isEmpty()) {
            return;
        }

        List<String> processedPages = pages.stream()
                .map(page -> page != null
                        ? page.replaceAll("(?i)" + java.util.regex.Pattern.quote(Notice.LINE_SEPARATOR) + "|\\{newline\\}", "\n")
                        : "")
                .collect(Collectors.toList());

        BookUtil.openBook(handler, title, author, processedPages, replacements);
    }
}
