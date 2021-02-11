package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface GovernanceProposalService {

    /**
     * Process a governance proposal. Creates a governance proposal record that is subsequently voted on.
     * @param request A governance proposal request
     * @return The DID of the resulting proposal
     */
    String processGovernanceProposal(String requestingStakeholderId, ProposeGovernanceDecisionRequest request);

    /**
     * Process a governance proposal. Creates a governance proposal record that is subsequently voted on.
     * @param proposal A governance proposal object
     * @return The DID of the resulting proposal
     */
    String processGovernanceProposal(GovernanceProposal proposal);

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

}
