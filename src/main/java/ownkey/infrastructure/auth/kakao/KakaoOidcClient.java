package ownkey.infrastructure.auth.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import ownkey.application.auth.dto.KakaoDTO;

@FeignClient(name = "kakao-oidc", url = "https://kauth.kakao.com")
public interface KakaoOidcClient {
    @GetMapping("/.well-known/jwks.json")
    KakaoDTO.OidcPublicKeyListResponse getPublicKeys();
}