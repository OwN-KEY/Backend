package ownkey.implement.notification.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ownkey.implement.common.model.BaseTimeEntity;

@Entity
@Getter
@Table(name = "notification_settings")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NotificationSetting extends BaseTimeEntity {

    @Id
    private Long userId;

    private boolean community;
    private boolean wiki;
    private boolean showmethekey;

    public NotificationSetting(Long userId) {
        this.userId = userId;
        this.community = true;
        this.wiki = true;
        this.showmethekey = true;
    }

    public void update(Boolean community, Boolean wiki, Boolean showmethekey) {
        if (community != null) this.community = community;
        if (wiki != null) this.wiki = wiki;
        if (showmethekey != null) this.showmethekey = showmethekey;
    }
}