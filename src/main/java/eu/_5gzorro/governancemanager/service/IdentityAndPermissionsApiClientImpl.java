package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.httpClient.CredentialClient;
import eu._5gzorro.governancemanager.httpClient.DIDClient;
import eu._5gzorro.governancemanager.httpClient.request.IssueCredentialRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityAndPermissionsApiClientImpl implements IdentityAndPermissionsApiClient {

    @Autowired
    private DIDClient didClient;

    @Autowired
    private CredentialClient credentialClient;

    @Override
    public void createDID(String handlerUrl, String authToken) {
        didClient.create(handlerUrl, authToken);
    }

    @Override
    public void issueCredential(IssueCredentialRequest request) {
        credentialClient.issue(request);
    }
}
