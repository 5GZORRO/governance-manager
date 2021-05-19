package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;

public interface IdentityAndPermissionsApiClient {
    void createDID(CreateDidRequest request);
    void createDID(CreateDidRequest request, String issuerStakeholderDid);
    void issueStakeholderCredential(RegisterStakeholderRequest request);
    void issueCredential(IssueCredentialRequest request);
}
