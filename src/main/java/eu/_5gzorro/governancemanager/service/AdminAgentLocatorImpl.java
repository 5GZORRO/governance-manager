package eu._5gzorro.governancemanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AdminAgentLocatorImpl implements AdminAgentLocator {

    @Value("${integrations.identity-permissions.myAgentBaseUrl}")
    private String myAgentBaseUrl;

    @Override
    public String getMyAdminAgentBaseUrl() {
        return myAgentBaseUrl;
    }

    @Override
    public String getAdminAgentBaseUrl() {
        return myAgentBaseUrl; // Use this until we have a means of service discovery
    }

    @Override
    public String getAdminAgentBaseUrl(String adminStakeholderDid) {
        return myAgentBaseUrl; // Use this until we have a means of service discovery
    }
}
