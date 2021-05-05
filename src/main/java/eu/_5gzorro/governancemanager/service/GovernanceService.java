package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.CredentialRequest;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface GovernanceService {
    Optional<UUID> processCredentialRequest(String requestingStakeholderId, CredentialRequest request) throws JsonProcessingException;
    boolean canIssueCredential(GovernanceProposal proposal);
    void issueCredential(GovernanceProposal proposal) throws IOException;

}
