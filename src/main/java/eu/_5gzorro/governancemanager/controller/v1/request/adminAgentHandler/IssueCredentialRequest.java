package eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler;

import com.fasterxml.jackson.annotation.JsonProperty;
import eu._5gzorro.governancemanager.dto.identityPermissions.CredentialSubjectDto;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

public class IssueCredentialRequest {

    @NotBlank
    @JsonProperty("_id")
    private String id;

    @NotBlank
    @JsonProperty("holder_request_id")
    private String requestId;

    @NotBlank
    private String type;

    @Valid
    private CredentialSubjectDto credentialSubject;

    @URL
    @NotBlank
    @JsonProperty("service_endpoint")
    private String serviceEndpoint;


    public IssueCredentialRequest() {
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public CredentialSubjectDto getCredentialSubject() {
        return credentialSubject;
    }

    public void setCredentialSubject(CredentialSubjectDto credentialSubject) {
        this.credentialSubject = credentialSubject;
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
        IssueCredentialRequest that = (IssueCredentialRequest) o;
        return id.equals(that.id) && requestId.equals(that.requestId) && type.equals(that.type) && credentialSubject.equals(that.credentialSubject) && serviceEndpoint.equals(that.serviceEndpoint);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, requestId, type, credentialSubject, serviceEndpoint);
    }

    @Override
    public String toString() {
        return "IssueCredentialRequest{" +
                "id='" + id + '\'' +
                ", requestId='" + requestId + '\'' +
                ", type='" + type + '\'' +
                ", credentialSubject=" + credentialSubject +
                ", serviceEndpoint='" + serviceEndpoint + '\'' +
                '}';
    }
}
