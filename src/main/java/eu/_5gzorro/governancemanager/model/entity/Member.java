package eu._5gzorro.governancemanager.model.entity;

import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="member", indexes = {
        @Index(name = "ix_name", columnList = "legal_name"),
})
public class Member {

    @Id
    private String id;

    @Column(name="legal_name", nullable = false, unique = true)
    private String legalName;

    private String address;

    @Column(name="status", nullable = false)
    private MembershipStatus status = MembershipStatus.PENDING;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MemberNotificationSetting> notificationSettings = new HashSet<>();

    @Column(nullable = false)
    private LocalDateTime created = LocalDateTime.now();
    private LocalDateTime updated;
    private LocalDateTime archived;

    public Member() {
    }

    public Member(String id, String legalName) {
        this.id = id;
        this.legalName = legalName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public Set<MemberNotificationSetting> getNotificationSettings() {
        return notificationSettings;
    }

    public void addNotificationSetting(MemberNotificationSetting setting) {
        notificationSettings.add(setting);
        setting.setMember(this);
    }

    public void addNotificationSettings(Set<MemberNotificationSetting> settings) {
        notificationSettings.addAll(settings);
        settings.forEach(s -> s.setMember(this));
    }

    public void removeNotificationSetting(MemberNotificationSetting setting) {
        setting.setMember(null);
        this.notificationSettings.remove(setting);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDateTime updated) {
        this.updated = updated;
    }

    public LocalDateTime getArchived() {
        return archived;
    }

    public void setArchived(LocalDateTime archived) {
        this.archived = archived;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return id.equals(member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, legalName);
    }

    @Override
    public String toString() {
        return "Member{" +
                "id='" + id + '\'' +
                ", legalName='" + legalName + '\'' +
                ", address='" + address + '\'' +
                ", status=" + status +
                ", created=" + created +
                ", updated=" + updated +
                ", archived=" + archived +
                '}';
    }
}
