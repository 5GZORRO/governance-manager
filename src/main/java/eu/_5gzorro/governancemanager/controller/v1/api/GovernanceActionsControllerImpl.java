package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.enumeration.ActionType;
import eu._5gzorro.governancemanager.model.enumeration.ProposalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class GovernanceActionsControllerImpl implements GovernanceActionsController {
    @Override
    public ResponseEntity<String> proposeGovernanceDecision(@Valid ProposeGovernanceDecisionRequest request) {
        return ResponseEntity
                .ok()
                .body("DID");
    }

    @Override
    public ResponseEntity<Page<GovernanceProposalDto>> getProposals(Pageable pageable, Optional<ProposalStatus> statusFilter, Optional<ActionType> actionTypeFilter) {
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
