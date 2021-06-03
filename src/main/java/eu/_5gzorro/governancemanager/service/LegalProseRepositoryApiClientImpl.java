package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.httpClient.LegalProseRepositoryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LegalProseRepositoryApiClientImpl implements LegalProseRepositoryApiClient {

    @Autowired
    LegalProseRepositoryClient lprClient;

    @Override
    public void setTemplateApprovalStatus(String did, boolean accept) {
        lprClient.setTemplateApprovalStatus(did, accept);
    }
}
