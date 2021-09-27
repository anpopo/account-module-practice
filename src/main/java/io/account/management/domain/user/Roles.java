package io.account.management.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Roles {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자"),
    BANNED("ROLE_BANNED", "차단된 사용자"),
    MANAGER("ROLE_MANAGER", "관리자");

    private final String key;
    private final String title;

}
