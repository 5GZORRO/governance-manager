package eu._5gzorro.governancemanager.controller.v1.request.membership;

import eu._5gzorro.governancemanager.model.NotificationMethodBase;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class NewMembershipRequest {
    @NotNull
    private String stakeholderId;

    @NotNull
    private String stakeholderClaimCertificate;

    @NotNull
    @Valid
    private NotificationMethodBase notificationMethod;

    public String getStakeholderId() {
        return stakeholderId;
    }

    public void setStakeholderId(String stakeholderId) {
        this.stakeholderId = stakeholderId;
    }

    public String getStakeholderClaimCertificate() {
        return stakeholderClaimCertificate;
    }

    public void setStakeholderClaimCertificate(String stakeholderClaimCertificate) {
        this.stakeholderClaimCertificate = stakeholderClaimCertificate;
    }

    public NotificationMethodBase getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(NotificationMethodBase notificationMethod) {
        this.notificationMethod = notificationMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewMembershipRequest that = (NewMembershipRequest) o;
        return stakeholderId.equals(that.stakeholderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stakeholderId);
    }

    @Override
    public String toString() {
        return "NewMembershipRequest{" +
                "stakeholderId='" + stakeholderId + '\'' +
                ", stakeholderClaimCertificate='" + stakeholderClaimCertificate + '\'' +
                ", notificationMethod=" + notificationMethod +
                '}';
    }
}
