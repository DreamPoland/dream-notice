package cc.dreamcode.notice;

import lombok.NonNull;
import java.util.Collection;

public interface NoticeAudienceResolver {

    NoticeAudience<?> resolveAudience(@NonNull Object target);

    Collection<NoticeAudience<?>> getAllAudiences();
}
