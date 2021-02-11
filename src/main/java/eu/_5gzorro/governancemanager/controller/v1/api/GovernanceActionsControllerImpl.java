package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.controller.v1.response.PagedGovernanceProposalsResponse;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.service.GovernanceProposalService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class GovernanceActionsControllerImpl implements GovernanceActionsController {

    private static final Logger log = LogManager.getLogger(GovernanceActionsControllerImpl.class);

    @Autowired
    private GovernanceProposalService governanceProposalService;

    @Override
    public ResponseEntity<String> proposeGovernanceDecision(@Valid ProposeGovernanceDecisionRequest request) {

        // TODO: Obtain requester's id from auth token
        String requestingStakeholderId = "RequestersId";

        String proposalIdentifier = governanceProposalService.processGovernanceProposal(requestingStakeholderId, request);

        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }

    @Override
    public ResponseEntity<PagedGovernanceProposalsResponse> getProposals(Pageable pageable, List<GovernanceProposalStatus> statusFilter, List<GovernanceActionType> actionTypeFilter) {

        Page<GovernanceProposalDto> pagedDto = governanceProposalService.getGovernanceProposals(pageable, actionTypeFilter, statusFilter);
        PagedGovernanceProposalsResponse response = new PagedGovernanceProposalsResponse(pagedDto);

        return ResponseEntity
                .ok()
                .body(response);
    }

    @Override
    public ResponseEntity<GovernanceProposalDto> getGovernanceProposal(@Valid String proposalId) {

        GovernanceProposalDto proposal = governanceProposalService.getGovernanceProposal(proposalId);
        return ResponseEntity
                .ok()
                .body(proposal);
    }

    @Override
    public ResponseEntity voteGovernanceDecision(@Valid String proposalId, @Valid boolean accept) {

        // TODO: Obtain requester's id from auth token
        String votingStakeholderId = "VotersId";

        governanceProposalService.voteOnGovernanceProposal(votingStakeholderId, proposalId, accept);

        return ResponseEntity
                .ok()
                .build();
    }
}
