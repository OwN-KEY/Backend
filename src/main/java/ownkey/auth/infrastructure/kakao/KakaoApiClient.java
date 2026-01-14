package ownkey.auth.infrastructure.kakao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "kakao-api", url = "https://kapi.kakao.com")
public interface KakaoApiClient {

    @PostMapping(value = "/v1/user/unlink", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void unlink(
            @RequestHeader("Authorization") String adminKeyHeader, // "KakaoAK " + adminKey
            @RequestParam("target_id_type") String targetIdType,   // "user_id"
            @RequestParam("target_id") String targetId             // kakaoId
    );
}