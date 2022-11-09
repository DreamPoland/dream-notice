package cc.dreamcode.notice.bungee.okaeri_serdes;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bungee.BungeeNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class BungeeNoticeSerdes implements ObjectSerializer<BungeeNotice> {

    @Override
    public boolean supports(@NonNull Class<? super BungeeNotice> type) {
        return BungeeNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BungeeNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getType());
        data.add("text", object.getText());
    }

    @Override
    public BungeeNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new BungeeNotice(
                data.get("type", NoticeType.class),
                data.get("text", String.class)
        );
    }
}
