package eu._5gzorro.governancemanager.model.entity;

import eu._5gzorro.governancemanager.model.enumeration.NotificationSetting;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name="member_notification_setting", uniqueConstraints = { @UniqueConstraint(columnNames = {"member_id", "notification_type", "setting" })})
public class MemberNotificationSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name="notification_type", nullable = false)
    private NotificationType notificationType;

    @Column(nullable = false)
    private NotificationSetting setting;

    @Column(nullable = false)
    private String value;

    public MemberNotificationSetting() {
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public NotificationType getNotificationType() {
        return notificationType;
    }

    public MemberNotificationSetting notificationType(NotificationType notificationType) {
        this.notificationType = notificationType;
        return this;
    }

    public MemberNotificationSetting setting(NotificationSetting setting) {
        this.setting = setting;
        return this;
    }

    public MemberNotificationSetting value(String value) {
        this.value = value;
        return this;
    }

    public NotificationSetting getSetting() {
        return setting;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MemberNotificationSetting that = (MemberNotificationSetting) o;
        return id == that.id && member.equals(that.member) && notificationType == that.notificationType && setting == that.setting && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, member, notificationType, setting, value);
    }

    @Override
    public String toString() {
        return "MemberNotificationSetting{" +
                ", notificationType=" + notificationType +
                ", setting=" + setting +
                ", value='" + value + '\'' +
                '}';
    }

}
