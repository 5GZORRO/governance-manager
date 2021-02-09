package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.controller.v1.response.PagedGovernanceProposalsResponse;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.ProposalStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
public class GovernanceActionsControllerImpl implements GovernanceActionsController {

    private static final Logger log = LogManager.getLogger(GovernanceActionsControllerImpl.class);

    @Override
    public ResponseEntity<String> proposeGovernanceDecision(@Valid ProposeGovernanceDecisionRequest request) {
        return ResponseEntity
                .ok()
                .body("DID");
    }

    @Override
    public ResponseEntity<PagedGovernanceProposalsResponse> getProposals(Pageable pageable, Optional<List<ProposalStatus>> statusFilter, Optional<List<GovernanceActionType>> actionTypeFilter) {
        return ResponseEntity
                .ok()
                .body(null);
    }

    @Override
    public ResponseEntity<GovernanceProposalDto> getGovernanceDecision(@Valid String proposalId) {
        return ResponseEntity
                .ok()
                .body(null);
    }

    @Override
    public ResponseEntity voteGovernanceDecision(@Valid String proposalId, @Valid boolean accept) {
        return ResponseEntity
                .ok().build();
    }
}
