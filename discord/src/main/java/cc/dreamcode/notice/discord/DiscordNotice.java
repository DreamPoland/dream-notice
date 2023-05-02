package cc.dreamcode.notice.discord;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public abstract class DiscordNotice {

    private final DiscordNoticeType type;
    private final Object value;

}
