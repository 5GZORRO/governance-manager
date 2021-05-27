package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.httpClient.CredentialClient;
import eu._5gzorro.governancemanager.httpClient.DIDClient;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import eu._5gzorro.governancemanager.model.exception.DIDCreationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IdentityAndPermissionsApiClientImpl implements IdentityAndPermissionsApiClient {

    private static final Logger log = LogManager.getLogger(IdentityAndPermissionsApiClientImpl.class);

    @Autowired
    private DIDClient didClient;

    @Autowired
    private CredentialClient credentialClient;

    private String authToken = "";//TODO - grab from ID&P?

    @Override
    public void createDID(CreateDidRequest request) {

        try {
            request.authToken(authToken);
            didClient.create(request);
        }
        catch(Exception ex) {
            log.error("Error creating DID", ex);
            throw new DIDCreationException(ex);
        }
    }

    @Override
    public void createDID(CreateDidRequest request, String issuerStakeholderDid) {
        try {
            request.authToken(authToken);
            didClient.create(request);
        }
        catch(Exception ex) {
            log.error("Error creating DID", ex);
            throw new DIDCreationException(ex);
        }
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
