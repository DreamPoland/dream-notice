package cc.dreamcode.notice.adventure;

import cc.dreamcode.notice.Notice;
import cc.dreamcode.notice.minecraft.NoticeImpl;
import cc.dreamcode.notice.minecraft.NoticeType;
import cc.dreamcode.utilities.builder.ListBuilder;
import eu.okaeri.placeholders.context.PlaceholderContext;
import lombok.NonNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEventSource;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class AdventureNotice<R extends Notice<R>> extends NoticeImpl<R> {

    private static final char ALT_COLOR_CHAR = '&';
    private static final Pattern FIELD_PATTERN = Pattern.compile("\\{(?<content>[^}]+)}");

    private Component joiningComponent = null;
    private List<Component> component = null;

    public AdventureNotice(NoticeType noticeType, String... noticeText) {
        super(noticeType, noticeText);
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

    public TextReplacementConfig getPlaceholderConfig() {

        if (!this.getPlaceholderContext().isPresent()) {
            return null;
        }

        PlaceholderContext placeholderContext = this.getPlaceholderContext().get();
        Map<String, String> renderedFields = placeholderContext.renderFields()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getRaw(),
                        Map.Entry::getValue
                ));

        return TextReplacementConfig.builder()
                .match(FIELD_PATTERN)
                .replacement((result, input) -> {
                    String fieldValue = ChatColor.translateAlternateColorCodes(ALT_COLOR_CHAR, renderedFields.get(result.group(1)));
                    return AdventureLegacy.component(fieldValue);
                })
                .build();
    }

    public List<Component> toSplitComponents() {

        if (this.component == null) {
            this.component = AdventureLegacy.splitDeserialize(this.getRaw(), this.getPlaceholderConfig());
        }

        return this.component;
    }

    public Component toJoiningComponent() {

        if (this.joiningComponent == null) {
            this.joiningComponent = AdventureLegacy.joiningDeserialize(this.getRaw(), this.getPlaceholderConfig());
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
