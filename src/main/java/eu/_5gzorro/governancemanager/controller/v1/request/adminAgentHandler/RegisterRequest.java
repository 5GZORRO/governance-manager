package eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler;

import eu._5gzorro.governancemanager.model.NotificationMethodBase;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RegisterRequest extends IssueRequest {

    @NotBlank
    private String legalName;

    @NotBlank
    private String address;

    @NotNull
    @Valid
    private NotificationMethodBase notificationMethod;

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
        if (!super.equals(o)) return false;
        RegisterRequest that = (RegisterRequest) o;
        return legalName.equals(that.legalName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), legalName);
    }

    @Override
    public String toString() {
        return "RegisterRequest{" +
                "legalName='" + legalName + '\'' +
                ", notificationMethod=" + notificationMethod +
                '}';
    }
}
