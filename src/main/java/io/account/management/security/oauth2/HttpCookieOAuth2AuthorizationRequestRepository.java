package io.account.management.security.oauth2;

import com.nimbusds.oauth2.sdk.util.StringUtils;
import io.account.management.util.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * - OAuth2 프로토콜에서는 CSRF 공격을 방지하기 위해 상태 매개 변수를 사용하는 것이 좋다.
 * - 인증 중에 응용 프로그램은 인증 요청에서 이 매개 변수를 보내고 OAuth2 공급자는 OAuth2 콜백에서 변경되지 않은 이 매개 변수를 반환한다.
 * - 응용 프로그램은 OAut2 공급자로부터 반환된 상태 매개 변수의 값을 처음에 전송한 값과 비교한다. 일치하지 않으면 인증 요청이 거부된다.
 * - 이 흐름을 달성하기 위해, 애플리케이션은 나중에 OAut2 제공자로부터 반환된 상태와 비교할 수 있도록 상태 매개변수를 어딘가에 저장할 필요가 있다.
 * - redirect_uri 뿐만 아니라 상태도 쿠키에 저장한다. 이 클래스는 인증 요청을 쿠키에 저장하고 검색하는 기능을 제공한다.
 */

@Component(value = "httpCookieOAuth2AuthorizationRequestRepository")
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    public static final String OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME = "oauth2_auth_request";
    public static final String REDIRECT_URI_PARAM_COOKIE_NAME = "redirect_uri";
    public static final int cookieExpireSeconds = 180;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            deleteCookie(request, response);
            return ;
        }

        CookieUtils.addCookie(
                response,
                OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME,
                CookieUtils.serialize(authorizationRequest),
                cookieExpireSeconds
        );

        String redirectUriAfterLogin = request.getParameter(REDIRECT_URI_PARAM_COOKIE_NAME);
        if (StringUtils.isNotBlank(redirectUriAfterLogin)) {
            CookieUtils.addCookie(response, REDIRECT_URI_PARAM_COOKIE_NAME, redirectUriAfterLogin, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        deleteCookie(request, response);
    }

    private void deleteCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, OAUTH2_AUTHORIZATION_REQUEST_COOKIE_NAME);
        CookieUtils.deleteCookie(request, response, REDIRECT_URI_PARAM_COOKIE_NAME);
    }
}
