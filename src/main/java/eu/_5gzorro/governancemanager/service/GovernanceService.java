package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;

import java.io.IOException;

public interface GovernanceService {
    boolean canIssueCredential(GovernanceProposal proposal);
    void issueCredential(GovernanceProposal proposal) throws IOException;

}
