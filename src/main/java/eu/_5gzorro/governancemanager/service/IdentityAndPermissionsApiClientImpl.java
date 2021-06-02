package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.dto.identityPermissions.StakeholderStatusDto;
import eu._5gzorro.governancemanager.httpClient.CredentialClient;
import eu._5gzorro.governancemanager.httpClient.DIDClient;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityAndPermissionsApiClientImpl implements IdentityAndPermissionsApiClient {

    @Autowired
    private DIDClient didClient;

    @Autowired
    private CredentialClient credentialClient;

    private StakeholderStatusDto myStakeholderStatus;

    @Override
    public void createDID(CreateDidRequest request) {
        request.authToken(getAuthToken());
        didClient.create(request);
    }

    private String getAuthToken() {
        if(myStakeholderStatus == null) {
            this.myStakeholderStatus = didClient.getMyStakeholderCredential();
        }
        return myStakeholderStatus.getAuthToken();
    }

    @Override
    public void issueStakeholderCredential(RegisterStakeholderRequest request) {
        credentialClient.issueStakeholderCredential(request.getId(), request);
    }

    @Override
    public void issueCredential(IssueCredentialRequest request) {
        credentialClient.issueCredential(request.getId(), request);
    }
}
