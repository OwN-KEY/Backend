package ownkey.application.auth.dto;

import ownkey.implement.auth.model.KakaoId;
import ownkey.implement.user.model.vo.Nickname;

public record OidcProfile(
        KakaoId kakaoId,
        Nickname nickname,
        String profileImage
) {}