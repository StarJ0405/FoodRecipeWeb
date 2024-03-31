package com.StarJ.food_recipe.Securities;

import com.StarJ.food_recipe.Entities.Users.PrincipalDetail;
import com.StarJ.food_recipe.Entities.Users.SiteUser;
import com.StarJ.food_recipe.Entities.Users.UserRepository;
import com.StarJ.food_recipe.Securities.UsersInfo.FacebookUserInfo;
import com.StarJ.food_recipe.Securities.UsersInfo.GoogleUserInfo;
import com.StarJ.food_recipe.Securities.UsersInfo.KakaoUserInfo;
import com.StarJ.food_recipe.Securities.UsersInfo.NaverUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {


    private UserRepository userRepository;

    //구글로 부터 받은 userRequest 데이터에 대한 후처리 되는 함수
    //함수종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo oAuth2UserInfo;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) // 구글 로그인 요청
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) // 페이스북 로그인 요청
            oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
        else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) // 네이버 로그인 요청
            oAuth2UserInfo = new NaverUserInfo(oAuth2User.getAttributes());
        else if (userRequest.getClientRegistration().getRegistrationId().equals("kakao")) // 카카오 로그인 요청
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes(), oAuth2User.getName());
        else // 미 지원 요청
            return null;


        String provider = oAuth2UserInfo.getProvider(); // google
        String providerId = oAuth2UserInfo.getProviderId();
        String id = provider + "_" + providerId;
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        Optional<SiteUser> optional = userRepository.findById(id);
        SiteUser user;
        if (optional.isEmpty()) {
            //강제 회원가입
            //회원 DB에 추가함
            //password 가 null 이기 때문에 일반적인 회원가입을 할 수가 없음
            user = SiteUser.builder()
                    .id(id)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(user);
        } else user = optional.get();

        return new PrincipalDetail(user, oAuth2User.getAttributes());
    }
}