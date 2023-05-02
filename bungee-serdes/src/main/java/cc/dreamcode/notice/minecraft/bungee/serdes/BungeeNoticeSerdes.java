package cc.dreamcode.notice.minecraft.bungee.serdes;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bungee.BungeeMinecraftNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class BungeeNoticeSerdes implements ObjectSerializer<BungeeMinecraftNotice> {

    @Override
    public boolean supports(@NonNull Class<? super BungeeMinecraftNotice> type) {
        return BungeeMinecraftNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BungeeMinecraftNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getType());
        data.add("text", object.getText());
    }

    @Override
    public BungeeMinecraftNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new BungeeMinecraftNotice(
                data.get("type", MinecraftNoticeType.class),
                data.get("text", String.class)
        );
    }
}
