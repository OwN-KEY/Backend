package ownkey.auth.infrastructure.kakao.dto;

import java.util.List;

public class KakaoDTO {

    public record OidcPublicKeyListResponse(List<Jwk> keys) {}

    public record Jwk(
            String kid,
            String kty,
            String alg,
            String use,
            String n,
            String e
    ) {}
}