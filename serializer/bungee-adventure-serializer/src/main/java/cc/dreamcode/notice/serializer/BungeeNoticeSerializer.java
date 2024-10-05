package cc.dreamcode.notice.serializer;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bungee.BungeeNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

import java.util.Locale;

public class BungeeNoticeSerializer implements ObjectSerializer<BungeeNotice> {
    @Override
    public boolean supports(@NonNull Class<? super BungeeNotice> type) {
        return BungeeNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BungeeNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getNoticeType(), NoticeType.class);
        data.add("text", object.getNoticeText(), String.class);

        if (!object.getLocale().equals(Locale.forLanguageTag("pl"))) {
            data.add("locale", object.getLocale().toLanguageTag());
        }

        if (object.getTitleFadeIn() != 10) {
            data.add("title-fade-in", object.getTitleFadeIn());
        }

        if (object.getTitleStay() != 20) {
            data.add("title-stay", object.getTitleStay());
        }

        if (object.getTitleFadeOut() != 10) {
            data.add("title-fade-out", object.getTitleFadeOut());
        }
    }

    @Override
    public BungeeNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        BungeeNotice bungeeNotice = new BungeeNotice(
                data.get("type", NoticeType.class),
                data.get("text", String.class)
        );

        if (data.containsKey("locale")) {
            bungeeNotice.setLocale(Locale.forLanguageTag(data.get("locale", String.class)));
        }

        if (data.containsKey("title-fade-in")) {
            bungeeNotice.setTitleFadeIn(data.get("title-fade-in", Integer.class));
        }

        if (data.containsKey("title-stay")) {
            bungeeNotice.setTitleStay(data.get("title-stay", Integer.class));
        }

        if (data.containsKey("title-fade-out")) {
            bungeeNotice.setTitleFadeOut(data.get("title-fade-out", Integer.class));
        }

        return bungeeNotice;
    }
}
