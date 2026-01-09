package ownkey.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ownkey.common.exception.BusinessException;
import ownkey.common.exception.ErrorCode;
import ownkey.domain.common.BaseTimeEntity;

import java.util.UUID;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String kakaoId;

    private String nickname;
    private String profileImageId;
    private String backgroundImageId;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // 회원가입용 생성자
    public User(String kakaoId, String nickname, String profileImageId) {
        this.kakaoId = kakaoId;
        this.nickname = nickname;
        this.profileImageId = profileImageId;
        this.role = UserRole.USER;
        this.status = UserStatus.ACTIVE;
    }

    public void updateProfile(String nickname, String profileImageId, String backgroundImageId) {
        if (this.status == UserStatus.DELETED) throw new BusinessException(ErrorCode.USER_NOT_FOUND);

        if (nickname != null && !nickname.isBlank()) {
            this.nickname = nickname;
        }
        if (profileImageId != null) {
            this.profileImageId = profileImageId;
        }
        if (backgroundImageId != null) {
            this.backgroundImageId = backgroundImageId;
        }
    }

    public void withdraw() {
        this.status = UserStatus.DELETED;
        this.nickname = "(알수없음)";
        this.kakaoId = "withdrawn_" + UUID.randomUUID();
        this.profileImageId = null;
        this.backgroundImageId = null;
    }
}