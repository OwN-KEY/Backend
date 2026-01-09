package ownkey.infrastructure.auth.kakao;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ownkey.application.auth.dto.KakaoDTO;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class IdTokenValidator {

    private final KakaoOidcClient kakaoOidcClient;
    private final JsonMapper jsonMapper;

    @Value("${kakao.client-id}")
    private String aud;

    private static final String ISS = "https://kauth.kakao.com";

    public Claims validateAndGetClaims(String idToken) {
        try {
            String headerPart = idToken.split("\\.")[0];
            String headerJson = new String(Base64.getUrlDecoder().decode(headerPart));
            JsonNode headerNode = jsonMapper.readTree(headerJson);
            String kid = headerNode.get("kid").asString();

            KakaoDTO.Jwk jwk = kakaoOidcClient.getPublicKeys().keys().stream()
                    .filter(k -> k.kid().equals(kid))
                    .findFirst()
                    .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

            BigInteger modulus = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.n()));
            BigInteger exponent = new BigInteger(1, Base64.getUrlDecoder().decode(jwk.e()));
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new RSAPublicKeySpec(modulus, exponent));

            Jws<Claims> jws = Jwts.parser()
                    .requireIssuer(ISS)
                    .requireAudience(aud)
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(idToken);

            return jws.getPayload();

        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}