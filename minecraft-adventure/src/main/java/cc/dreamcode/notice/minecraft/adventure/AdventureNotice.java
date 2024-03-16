package cc.dreamcode.notice.minecraft.adventure;

import cc.dreamcode.notice.DreamNotice;
import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;

import java.util.Map;

public class AdventureNotice<R extends DreamNotice<R>> extends MinecraftNotice<R> {

    private Component component = null;

    public AdventureNotice(MinecraftNoticeType noticeType, String... noticeText) {
        super(noticeType, noticeText);
    }

    @Override
    public R with(@NonNull String from, @NonNull String to) {

        R respond = super.with(from, to);

        if (this.component != null) {
            this.component = AdventureLegacy.component(this.getRender());
        }

        return respond;
    }

    @Override
    public R with(@NonNull Map<String, Object> replaceMap) {
        R respond = super.with(replaceMap);

        if (this.component != null) {
            this.component = AdventureLegacy.component(this.getRender());
        }

        return respond;
    }

    @SuppressWarnings("unchecked")
    public R hoverEvent(@NonNull HoverEventSource<?> source) {
        this.component = this.component.hoverEvent(source);
        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R clickEvent(@NonNull ClickEvent event) {
        this.component = this.component.clickEvent(event);
        return (R) this;
    }

    public Component toComponent() {

        if (this.component == null) {
            this.component = AdventureLegacy.component(this.getRender());
        }

        return this.component;
    }
}
