package cc.dreamcode.notice.serializer;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.NoticeType;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class AbstractNoticeSerializer<N extends Notice<?>> implements ObjectSerializer<N> {

    @Override
    public void serialize(@NonNull N object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        NoticeType type = object.getNoticeType();
        List<String> text = object.getText();

        if (type == NoticeType.CHAT && text != null) {
            if (text.size() == 1) {
                data.setValue(text.get(0));
                return;
            }
            if (text.size() > 1) {
                data.setValue(text);
                return;
            }
        }

        data.add("type", type != null ? type.getName() : "CHAT");

        if (text != null && !text.isEmpty()) {
            if (text.size() == 1) {
                data.add("text", text.get(0));
            } else {
                data.add("text", text);
            }
        }

        if (object.getTitle() != null) {
            data.add("title", object.getTitle());
        }
        if (object.getSubtitle() != null) {
            data.add("subtitle", object.getSubtitle());
        }
        if (object.getHeader() != null) {
            data.add("header", object.getHeader());
        }
        if (object.getFooter() != null) {
            data.add("footer", object.getFooter());
        }

        if (object.getFadeIn() != 10) {
            data.add("fadeIn", object.getFadeIn());
        }
        if (object.getStay() != 70) {
            data.add("stay", object.getStay());
        }
        if (object.getFadeOut() != 20) {
            data.add("fadeOut", object.getFadeOut());
        }

        if (object.getBossBarColor() != null && !object.getBossBarColor().equals("WHITE")) {
            data.add("bossBarColor", object.getBossBarColor());
        }
        if (object.getBossBarOverlay() != null && !object.getBossBarOverlay().equals("PROGRESS")) {
            data.add("bossBarOverlay", object.getBossBarOverlay());
        }
        if (object.getBossBarProgress() != 1.0f) {
            data.add("bossBarProgress", object.getBossBarProgress());
        }

        if (object.getBookAuthor() != null) {
            data.add("bookAuthor", object.getBookAuthor());
        }
        if (object.getPages() != null && !object.getPages().isEmpty()) {
            data.add("pages", object.getPages());
        }

        if (object.getSoundKey() != null) {
            data.add("soundKey", object.getSoundKey());
        }
        if (object.getSoundCategory() != null && !object.getSoundCategory().equals("MASTER")) {
            data.add("soundCategory", object.getSoundCategory());
        }
        if (object.getVolume() != 1.0f) {
            data.add("volume", object.getVolume());
        }
        if (object.getPitch() != 1.0f) {
            data.add("pitch", object.getPitch());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public N deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        if (data.isValue()) {
            Object value = data.getValueRaw();
            if (value instanceof String str) {
                return createChat(Notice.prepareText(Collections.singletonList(str)));
            } else if (value instanceof List<?> list) {
                List<String> stringList = new ArrayList<>();
                for (Object item : list) {
                    stringList.add(item != null ? item.toString() : "");
                }
                return createChat(Notice.prepareText(stringList));
            }
        }

        var builder = Notice.builder();

        if (data.containsKey("type")) {
            String typeStr = data.get("type", String.class);
            builder.noticeType(NoticeType.fromName(typeStr));
        }

        if (data.containsKey("text")) {
            Object textObj = data.getRaw("text");
            if (textObj instanceof List<?> list) {
                List<String> stringList = new ArrayList<>();
                for (Object item : list) {
                    stringList.add(item != null ? item.toString() : "");
                }
                builder.text(Notice.prepareText(stringList));
            } else if (textObj != null) {
                builder.text(Notice.prepareText(Collections.singletonList(textObj.toString())));
            }
        }

        if (data.containsKey("title")) {
            String title = data.get("title", String.class);
            if (Notice.containsLineSeparator(title) && !data.containsKey("subtitle")) {
                String[] split = Notice.splitLineSeparator(title, 2);
                builder.title(split[0]);
                if (split.length > 1) {
                    builder.subtitle(split[1]);
                }
            } else {
                builder.title(title);
            }
        }
        if (data.containsKey("subtitle")) {
            builder.subtitle(data.get("subtitle", String.class));
        }
        if (data.containsKey("header")) {
            builder.header(data.get("header", String.class));
        }
        if (data.containsKey("footer")) {
            builder.footer(data.get("footer", String.class));
        }

        if (data.containsKey("fadeIn")) {
            builder.fadeIn(data.get("fadeIn", Integer.class));
        }
        if (data.containsKey("stay")) {
            builder.stay(data.get("stay", Integer.class));
        }
        if (data.containsKey("fadeOut")) {
            builder.fadeOut(data.get("fadeOut", Integer.class));
        }

        if (data.containsKey("bossBarColor")) {
            builder.bossBarColor(data.get("bossBarColor", String.class));
        }
        if (data.containsKey("bossBarOverlay")) {
            builder.bossBarOverlay(data.get("bossBarOverlay", String.class));
        }
        if (data.containsKey("bossBarProgress")) {
            builder.bossBarProgress(data.get("bossBarProgress", Float.class));
        }

        if (data.containsKey("bookAuthor")) {
            builder.bookAuthor(data.get("bookAuthor", String.class));
        }
        if (data.containsKey("pages")) {
            builder.pages(data.getAsList("pages", String.class));
        }

        if (data.containsKey("soundKey")) {
            builder.soundKey(data.get("soundKey", String.class));
        }
        if (data.containsKey("soundCategory")) {
            builder.soundCategory(data.get("soundCategory", String.class));
        }
        if (data.containsKey("volume")) {
            builder.volume(data.get("volume", Float.class));
        }
        if (data.containsKey("pitch")) {
            builder.pitch(data.get("pitch", Float.class));
        }

        return createFromNotice(builder.build());
    }

    protected abstract N createChat(@NonNull List<String> text);

    protected abstract N createFromNotice(@NonNull Notice<?> notice);
}
