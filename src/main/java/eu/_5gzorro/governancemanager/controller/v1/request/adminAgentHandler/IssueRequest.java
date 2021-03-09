package eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler;

import eu._5gzorro.governancemanager.dto.identityPermissions.ClaimDto;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Objects;

public class IssueRequest{

    @NotBlank
    private String stakeholderId;

    @NotBlank
    private String subjectId;

    @NotBlank
    private String requestId;

    private List<ClaimDto> claims;

    public IssueRequest() {
    }

    public String getStakeholderId() {
        return stakeholderId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<ClaimDto> getClaims() {
        return claims;
    }

    public void setStakeholderId(String stakeholderId) {
        this.stakeholderId = stakeholderId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setClaims(List<ClaimDto> claims) {
        this.claims = claims;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IssueRequest that = (IssueRequest) o;
        return stakeholderId.equals(that.stakeholderId) && subjectId.equals(that.subjectId) && requestId.equals(that.requestId) && Objects.equals(claims, that.claims);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stakeholderId, subjectId, requestId, claims);
    }

    @Override
    public String toString() {
        return "IssueRequest{" +
                "stakeholderId='" + stakeholderId + '\'' +
                ", subjectId='" + subjectId + '\'' +
                ", requestId='" + requestId + '\'' +
                ", claims=" + claims +
                '}';
    }
}
