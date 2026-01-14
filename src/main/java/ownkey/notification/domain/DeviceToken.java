package ownkey.notification.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.Assert;
import ownkey.common.domain.BaseTimeEntity;

@Entity
@Getter
@Table(name = "device_tokens")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeviceToken extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceEnvironment environment;

    public DeviceToken(Long userId, String token, String environment) {
        Assert.notNull(userId, "UserId must not be null");
        Assert.hasText(token, "Token must not be empty");

        this.userId = userId;
        this.token = token;
        this.environment = DeviceEnvironment.from(environment);
    }
}