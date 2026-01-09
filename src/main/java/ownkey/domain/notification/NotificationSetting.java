package ownkey.domain.notification;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ownkey.domain.common.BaseTimeEntity;

@Entity
@Getter
@Table(name = "notification_settings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting extends BaseTimeEntity {

    @Id
    private Long userId;

    private boolean community = true;
    private boolean wiki = true;
    private boolean showmethekey = true;

    public NotificationSetting(Long userId) {
        this.userId = userId;
    }

    public void update(boolean community, boolean wiki, boolean showmethekey) {
        this.community = community;
        this.wiki = wiki;
        this.showmethekey = showmethekey;
    }
}