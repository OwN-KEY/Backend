package ownkey.implement.auth.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ownkey.application.auth.dto.KakaoDTO;
import ownkey.infrastructure.auth.kakao.KakaoOidcClient;
import ownkey.presentation.common.exception.BusinessException;
import ownkey.presentation.common.exception.ErrorCode;
import ownkey.application.auth.dto.OidcProfile;
import ownkey.implement.auth.model.AuthToken;
import ownkey.implement.auth.model.KakaoId;
import ownkey.implement.user.model.vo.Nickname;
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

    public OidcProfile validate(AuthToken idToken) {
        try {
            String tokenValue = idToken.value();
            String headerPart = tokenValue.split("\\.")[0];
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
                    .parseSignedClaims(tokenValue);

            Claims payload = jws.getPayload();

            String nicknameValue = payload.get("nickname", String.class);
            if (nicknameValue == null || nicknameValue.isBlank()) {
                // Kakao ID(sub)의 뒷 4자리를 따서 임시 닉네임 생성 (예: 오키유저8362)
                String sub = payload.getSubject();
                nicknameValue = "온키유저" + (sub.length() > 4 ? sub.substring(sub.length() - 4) : sub);
            }

            return new OidcProfile(
                    new KakaoId(payload.getSubject()),
                    new Nickname(nicknameValue),
                    payload.get("picture", String.class)
            );

        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
    }
}