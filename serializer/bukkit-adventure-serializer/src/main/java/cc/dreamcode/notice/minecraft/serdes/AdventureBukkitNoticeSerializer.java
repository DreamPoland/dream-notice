package cc.dreamcode.notice.minecraft.serdes;

import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.notice.minecraft.adventure.bukkit.AdventureBukkitNotice;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;

public class AdventureBukkitNoticeSerializer implements ObjectSerializer<AdventureBukkitNotice> {
    @Override
    public boolean supports(@NonNull Class<? super AdventureBukkitNotice> type) {
        return AdventureBukkitNotice.class.isAssignableFrom(type);
    }

    @Override
    public void serialize(@NonNull AdventureBukkitNotice object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("type", object.getNoticeType(), MinecraftNoticeType.class);
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
    public AdventureBukkitNotice deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {

        AdventureBukkitNotice minecraftNotice = new AdventureBukkitNotice(
                data.get("type", MinecraftNoticeType.class),
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
