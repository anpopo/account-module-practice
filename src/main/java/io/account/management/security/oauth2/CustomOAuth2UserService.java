package io.account.management.security.oauth2;

import io.account.management.domain.user.AuthProvider;
import io.account.management.domain.user.Users;
import io.account.management.domain.user.UsersRepository;
import io.account.management.exception.OAuth2AuthenticationProcessingException;
import io.account.management.security.UserPrincipal;
import io.account.management.security.oauth2.user.OAuth2UserInfo;
import io.account.management.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * Spring Security 의 DefaultOAuth2UserService 를 상속받아서 loadUser() 메서드를 재정의 한다.
 * - 이 메서드는 OAuth2 공급자에서 액세스 토큰을 가져온 후 호출된다.
 * - 먼저 OAuth2 공급자에서 사용자의 세부 정보를 가져온다.
 * - 동일한 email 을 가진 사용자가 데이터베이스에 이미 있으면 세부 정보를 업데이트하고 그렇지 않으면 새 사용자를 등록한다.
 */

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UsersRepository usersRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        try {
            return processOAuth2User(userRequest, oAuth2User);
        } catch (AuthenticationException e) {
            throw e;
        } catch (Exception ex) {
            //AuthenticationException 의 인스턴스를 throw 하게 되면 OAuth2AuthenticationFailureHandler 를 활성되게 한다..
            throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
        }
    }

    private OAuth2User processOAuth2User(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
            throw new OAuth2AuthenticationProcessingException("Email not found from OAuth2 provider");
        }

        Optional<Users> usersOptional = usersRepository.findByEmail(oAuth2UserInfo.getEmail());

        Users users;

        if (usersOptional.isPresent()) {
            users = usersOptional.get();
            if (!users.getProvider().equals(AuthProvider.valueOf(registrationId))) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " + users.getProvider() + " account. " +
                        "Please use your " + users.getProvider() + " account to login.");
            }

            users = updateExistingUser(users, oAuth2UserInfo);
        } else {
            users = registerNewUser(userRequest, oAuth2UserInfo);
        }
        return UserPrincipal.create(users, oAuth2User.getAttributes());
    }

    private Users registerNewUser(OAuth2UserRequest userRequest, OAuth2UserInfo oAuth2UserInfo) {
        Users users = Users.builder()
                .provider(AuthProvider.valueOf(userRequest.getClientRegistration().getRegistrationId()))
                .providerId(oAuth2UserInfo.getId())
                .name(oAuth2UserInfo.getName())
                .email(oAuth2UserInfo.getEmail())
                .imageUrl(oAuth2UserInfo.getImageUrl())
                .build();

        return usersRepository.save(users);
    }

    private Users updateExistingUser(Users users, OAuth2UserInfo oAuth2UserInfo) {
        users.updateNameAndImageUrl(oAuth2UserInfo.getName(), oAuth2UserInfo.getImageUrl());
        return usersRepository.save(users);
    }
}
