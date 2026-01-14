package ownkey.user.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.common.domain.BaseTimeEntity;
import ownkey.auth.domain.KakaoId;
import ownkey.user.domain.vo.Nickname;

import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "kakao_id", unique = true, nullable = false))
    private KakaoId kakaoId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "nickname"))
    private Nickname nickname;

    private String profileImageId;
    private String backgroundImageId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    public User(KakaoId kakaoId, Nickname nickname, String profileImageId) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    public void updateProfile(Nickname newNickname, String newProfileImageId, String newBackgroundImageId) {
        if (this.status == UserStatus.DELETED) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (newNickname != null) {
            this.nickname = newNickname;
        }
        if (newProfileImageId != null && !newProfileImageId.isBlank()) {
            this.profileImageId = newProfileImageId;
        }
        if (newBackgroundImageId != null && !newBackgroundImageId.isBlank()) {
            this.backgroundImageId = newBackgroundImageId;
        }
    }

    public void withdraw() {
        this.status = UserStatus.DELETED;
        this.nickname = new Nickname("(알수없음)");
        this.kakaoId = new KakaoId("withdrawn_" + UUID.randomUUID());
        this.profileImageId = null;
        this.backgroundImageId = null;
    }
}