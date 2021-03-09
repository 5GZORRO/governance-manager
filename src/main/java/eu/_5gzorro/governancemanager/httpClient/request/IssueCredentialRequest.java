package eu._5gzorro.governancemanager.httpClient.request;

import eu._5gzorro.governancemanager.dto.identityPermissions.ClaimDto;

import java.util.List;

public class IssueCredentialRequest {
    private String requestId;
    private List<ClaimDto> claims;
}
