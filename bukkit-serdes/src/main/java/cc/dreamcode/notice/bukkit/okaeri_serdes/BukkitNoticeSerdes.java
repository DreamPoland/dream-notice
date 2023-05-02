package cc.dreamcode.notice.bukkit.okaeri_serdes;

import cc.dreamcode.notice.NoticeType;
import cc.dreamcode.notice.bukkit.BukkitNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class BukkitNoticeSerdes implements ObjectSerializer<BukkitNotice> {

    @Override
    public boolean supports(@NonNull Class<? super BukkitNotice> type) {
        return BukkitNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BukkitNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getType());
        data.add("text", object.getText());
    }

    @Override
    public BukkitNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new BukkitNotice(
                data.get("type", NoticeType.class),
                data.get("text", String.class)
        );
    }
}
