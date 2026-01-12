package ownkey.implement.auth.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ownkey.implement.auth.model.KakaoId;
import ownkey.infrastructure.auth.kakao.KakaoApiClient;

@Component
@RequiredArgsConstructor
public class SocialAccountManager {

    private final KakaoApiClient kakaoApiClient;

    @Value("${kakao.admin-key}")
    private String adminKey;

    public void unlink(KakaoId kakaoId) {
        kakaoApiClient.unlink("KakaoAK " + adminKey, "user_id", kakaoId.value());
    }
}