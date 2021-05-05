package eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu._5gzorro.governancemanager.dto.identityPermissions.StakeholderClaimDto;
import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

public class RegisterStakeholderRequest extends CredentialRequest implements Serializable {

    @NotBlank
    @JsonProperty("_id")
    private String id;

    @NotBlank
    @JsonProperty("holder_request_id")
    private String requestId;

    @Valid
    @NotNull
    private StakeholderClaimDto stakeholderClaim;

    private String timestamp;

    @URL
    @NotBlank
    @JsonProperty("service_endpoint")
    private String serviceEndpoint;

    public RegisterStakeholderRequest() {
        super(CredentialRequestType.STAKEHOLDER);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public StakeholderClaimDto getStakeholderClaim() {
        return stakeholderClaim;
    }

    public void setStakeholderClaim(StakeholderClaimDto stakeholderClaim) {
        this.stakeholderClaim = stakeholderClaim;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getServiceEndpoint() {
        return serviceEndpoint;
    }

    public void setServiceEndpoint(String serviceEndpoint) {
        this.serviceEndpoint = serviceEndpoint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterStakeholderRequest that = (RegisterStakeholderRequest) o;
        return id.equals(that.id) && requestId.equals(that.requestId) && getType().equals(that.getType()) && stakeholderClaim.equals(that.stakeholderClaim) && Objects.equals(timestamp, that.timestamp) && Objects.equals(serviceEndpoint, that.serviceEndpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestId, getType(), stakeholderClaim, timestamp, serviceEndpoint);
    }

    @Override
    public String toString() {
        return "RegisterStakeholderRequest{" +
                "id='" + id + '\'' +
                ", requestId='" + requestId + '\'' +
                ", type='" + getType() + '\'' +
                ", stakeholderClaim=" + stakeholderClaim +
                ", timestamp='" + timestamp + '\'' +
                ", serviceEndpoint='" + serviceEndpoint + '\'' +
                '}';
    }
}
