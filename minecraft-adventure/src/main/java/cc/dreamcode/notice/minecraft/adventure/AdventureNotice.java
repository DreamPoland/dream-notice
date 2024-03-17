package cc.dreamcode.notice.minecraft.adventure;

import cc.dreamcode.notice.DreamNotice;
import cc.dreamcode.notice.minecraft.MinecraftNotice;
import cc.dreamcode.notice.minecraft.MinecraftNoticeType;
import cc.dreamcode.utilities.builder.ListBuilder;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdventureNotice<R extends DreamNotice<R>> extends MinecraftNotice<R> {

    private Component joiningComponent = null;
    private List<Component> component = null;

    public AdventureNotice(MinecraftNoticeType noticeType, String... noticeText) {
        super(noticeType, noticeText);
    }

    @Override
    public R with(@NonNull String from, @NonNull Object to) {

        R respond = super.with(from, to);

        if (this.joiningComponent != null) {
            this.joiningComponent = AdventureLegacy.joiningDeserialize(this.getRender());
        }

        if (this.component != null) {
            this.component = AdventureLegacy.splitDeserialize(this.getRender());
        }

        return respond;
    }

    @Override
    public R with(@NonNull Map<String, Object> replaceMap) {
        R respond = super.with(replaceMap);

        if (this.joiningComponent != null) {
            this.joiningComponent = AdventureLegacy.joiningDeserialize(this.getRender());
        }

        if (this.component != null) {
            this.component = AdventureLegacy.splitDeserialize(this.getRender());
        }

        return respond;
    }

    @SuppressWarnings("unchecked")
    public R hoverEvent(@NonNull HoverEventSource<?> source) {
        this.joiningComponent = this.toJoiningComponent().hoverEvent(source);

        this.component = this.toSplitComponents()
                .stream()
                .map(scan -> scan.hoverEvent(source))
                .collect(Collectors.toList());

        return (R) this;
    }

    @SuppressWarnings("unchecked")
    public R clickEvent(@NonNull ClickEvent event) {
        this.joiningComponent = this.toJoiningComponent().clickEvent(event);

        this.component = this.toSplitComponents()
                .stream()
                .map(scan -> scan.clickEvent(event))
                .collect(Collectors.toList());

        return (R) this;
    }

    public List<Component> toSplitComponents() {

        if (this.component == null) {
            this.component = AdventureLegacy.splitDeserialize(this.getRender());
        }

        return this.component;
    }

    public Component toJoiningComponent() {

        if (this.joiningComponent == null) {
            this.joiningComponent = AdventureLegacy.joiningDeserialize(this.getRender());
        }

        return this.joiningComponent;
    }

    public List<Component> toComponents() {
        return new ListBuilder<Component>()
                .add(this.toJoiningComponent())
                .addAll(this.toSplitComponents())
                .build();
    }

    @Override
    public R clearRender() {
        R respond = super.clearRender();

        this.component = null;
        this.joiningComponent = null;
        return respond;
    }
}
