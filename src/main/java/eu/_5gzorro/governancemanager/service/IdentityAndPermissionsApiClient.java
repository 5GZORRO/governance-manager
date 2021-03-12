package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;

public interface IdentityAndPermissionsApiClient {
    void createDID(String callbackUrl, String authToken);
    void issueStakeholderCredential(RegisterStakeholderRequest request);
    void issueCredential(IssueCredentialRequest request);
}
