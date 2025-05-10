package cc.dreamcode.notice.serializer;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.paper.PaperNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

import java.util.Locale;

public class PaperNoticeSerializer implements ObjectSerializer<PaperNotice> {
    @Override
    public boolean supports(@NonNull Class<? super PaperNotice> type) {
        return PaperNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull PaperNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
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
    public PaperNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        PaperNotice paperNotice = new PaperNotice(
                data.get("type", NoticeType.class),
                data.get("text", String.class)
        );

        if (data.containsKey("locale")) {
            paperNotice.setLocale(Locale.forLanguageTag(data.get("locale", String.class)));
        }

        if (data.containsKey("title-fade-in")) {
            paperNotice.setTitleFadeIn(data.get("title-fade-in", Integer.class));
        }

        if (data.containsKey("title-stay")) {
            paperNotice.setTitleStay(data.get("title-stay", Integer.class));
        }

        if (data.containsKey("title-fade-out")) {
            paperNotice.setTitleFadeOut(data.get("title-fade-out", Integer.class));
        }

        return paperNotice;
    }
}
