package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface GovernanceProposalService {

    /**
     * Process a governance proposal. Creates a governance proposal record that is subsequently voted on.
     * @param request A governance proposal request
     * @return The DID of the resulting proposal
     */
    UUID processGovernanceProposal(String requestingStakeholderId, ProposeGovernanceDecisionRequest request);

    /**
     * Process a governance proposal. Creates a governance proposal record that is subsequently voted on.
     * @param proposal A governance proposal object
     * @return The DID of the resulting proposal
     */
    UUID processGovernanceProposal(GovernanceProposal proposal);

    UUID processIssueCredentialRequest(IssueCredentialRequest request) throws JsonProcessingException;

    Page<GovernanceProposalDto> getGovernanceProposals(Pageable pageable, List<GovernanceActionType> actionTypes, List<GovernanceProposalStatus> statuses);
    GovernanceProposalDto getGovernanceProposal(String id);

    /**
     * Process a vote cast against a proposal according to the governance model.
     * If voting threshold has been reached, then the proposal record will be updated to reflect the outcome.
     * @param votingStakeholderId
     * @param id
     * @param accept
     */
    void voteOnGovernanceProposal(String votingStakeholderId, String id, boolean accept);

    /**
     * Replace temporary proposalHandle with issued DID
     * @param proposalHandle
     * @param state
     */
    void completeGovernanceProposalCreation(UUID proposalHandle, DIDStateDto state) throws IOException;
}
