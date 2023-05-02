package cc.dreamcode.notice.minecraft.bukkit.serdes;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.bukkit.BukkitMinecraftNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class BukkitNoticeSerdes implements ObjectSerializer<BukkitMinecraftNotice> {

    @Override
    public boolean supports(@NonNull Class<? super BukkitMinecraftNotice> type) {
        return BukkitMinecraftNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull BukkitMinecraftNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getType());
        data.add("text", object.getText());
    }

    @Override
    public BukkitMinecraftNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        return new BukkitMinecraftNotice(
                data.get("type", MinecraftNoticeType.class),
                data.get("text", String.class)
        );
    }
}
