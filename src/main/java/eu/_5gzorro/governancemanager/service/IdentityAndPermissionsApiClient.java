package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.httpClient.request.IssueCredentialRequest;

public interface IdentityAndPermissionsApiClient {
    void createDID(String callbackUrl, String authToken);
    void issueCredential(IssueCredentialRequest request);
}
