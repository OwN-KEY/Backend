package ownkey.auth.domain;

import ownkey.user.domain.vo.Nickname;

public record OidcProfile(
        KakaoId kakaoId,
        Nickname nickname,
        String profileImage
) {}