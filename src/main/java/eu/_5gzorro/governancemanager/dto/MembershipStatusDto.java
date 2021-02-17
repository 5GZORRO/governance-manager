package eu._5gzorro.governancemanager.dto;

import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class MembershipStatusDto {
    private String stakeholderId;
    private MembershipStatus status;
    private LocalDateTime statusUpdated;

    public MembershipStatusDto() {
    }

    public String getStakeholderId() {
        return stakeholderId;
    }

    public void setStakeholderId(String stakeholderId) {
        this.stakeholderId = stakeholderId;
    }

    public MembershipStatus getStatus() {
        return status;
    }

    public void setStatus(MembershipStatus status) {
        this.status = status;
    }

    public LocalDateTime getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(LocalDateTime statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MembershipStatusDto that = (MembershipStatusDto) o;
        return stakeholderId.equals(that.stakeholderId) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stakeholderId, status);
    }

    @Override
    public String toString() {
        return "MembershipStatusDto{" +
                "stakeholderId='" + stakeholderId + '\'' +
                ", status=" + status +
                ", statusUpdated=" + statusUpdated +
                '}';
    }
}
