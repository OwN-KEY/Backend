package ownkey.auth.implement;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ownkey.auth.domain.KakaoId;
import ownkey.auth.infrastructure.kakao.KakaoApiClient;

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