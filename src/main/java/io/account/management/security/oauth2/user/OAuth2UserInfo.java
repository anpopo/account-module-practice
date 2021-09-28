package io.account.management.security.oauth2.user;

import java.util.Map;

/**
 * - 모든 OAuth2 공급자는 인증된 사용자의 세부 정보를 가져올 때 서로 다른 JSON 응답을 반환한다.
 * - Spring Security 는 응답에 대해 일반 map 형식으로 구문을 parse 한다.
 */
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();
    public abstract String getName();
    public abstract String getEmail();
    public abstract String getImageUrl();
}
