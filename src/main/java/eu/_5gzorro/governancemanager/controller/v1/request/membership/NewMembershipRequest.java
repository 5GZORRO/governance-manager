package eu._5gzorro.governancemanager.controller.v1.request.membership;

import eu._5gzorro.governancemanager.model.NotificationMethodBase;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class NewMembershipRequest {
    @NotBlank
    private String stakeholderId;

    @NotBlank
    private String legalName;

    @NotBlank
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

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
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
        return stakeholderId.equals(that.stakeholderId) && legalName.equals(that.legalName) && stakeholderClaimCertificate.equals(that.stakeholderClaimCertificate) && notificationMethod.equals(that.notificationMethod);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stakeholderId, legalName, stakeholderClaimCertificate, notificationMethod);
    }

    @Override
    public String toString() {
        return "NewMembershipRequest{" +
                "stakeholderId='" + stakeholderId + '\'' +
                ", legalName='" + legalName + '\'' +
                ", stakeholderClaimCertificate='" + stakeholderClaimCertificate + '\'' +
                ", notificationMethod=" + notificationMethod +
                '}';
    }
}
