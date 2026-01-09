package ownkey.domain.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.NonNull;
import ownkey.domain.common.BaseTimeEntity;

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

    @Column(nullable = false)
    private String environment;

    public DeviceToken(@NonNull Long userId, @NonNull String token, String environment) {
        this.userId = userId;
        this.token = token;
        this.environment = (environment != null) ? environment : "production";
    }
}