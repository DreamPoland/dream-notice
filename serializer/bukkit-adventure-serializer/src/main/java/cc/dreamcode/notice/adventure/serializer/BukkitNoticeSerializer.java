package cc.dreamcode.notice.adventure.serializer;

import cc.dreamcode.notice.minecraft.NoticeType;
import cc.dreamcode.notice.adventure.BukkitNoticeImpl;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class BukkitNoticeSerializer implements ObjectSerializer<BukkitNoticeImpl> {
    @Override
    public boolean supports(@NonNull Class<? super BukkitNoticeImpl> type) {
        return BukkitNoticeImpl.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BukkitNoticeImpl object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getNoticeType(), NoticeType.class);
        data.add("text", object.getRaw(), String.class);

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
    public BukkitNoticeImpl deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        BukkitNoticeImpl minecraftNotice = new BukkitNoticeImpl(
                data.get("type", NoticeType.class),
                data.get("text", String.class)
        );

        if (data.containsKey("title-fade-in")) {
            minecraftNotice.setTitleFadeIn(data.get("title-fade-in", Integer.class));
        }

        if (data.containsKey("title-stay")) {
            minecraftNotice.setTitleStay(data.get("title-stay", Integer.class));
        }

        if (data.containsKey("title-fade-out")) {
            minecraftNotice.setTitleFadeOut(data.get("title-fade-out", Integer.class));
        }

        return minecraftNotice;
    }
}
