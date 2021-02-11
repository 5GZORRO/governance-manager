package eu._5gzorro.governancemanager.model.exception;

import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class GovernanceProposalStatusException extends RuntimeException {

    private final Set<GovernanceProposalStatus> permittedStatuses;
    private final GovernanceProposalStatus actualStatus;

    public GovernanceProposalStatusException(Collection<GovernanceProposalStatus> permittedStatuses, GovernanceProposalStatus actualStatus) {
        this.permittedStatuses = new HashSet<>(permittedStatuses);
        this.actualStatus = actualStatus;
    }

    public GovernanceProposalStatusException(GovernanceProposalStatus permittedStatus, GovernanceProposalStatus actualStatus) {
        this(new HashSet<>(Collections.singleton(permittedStatus)), actualStatus);
    }

    @Override
    public String getMessage() {
        return String.format("This operation is not permitted on a Governance Proposal in %s state. Permitted states: %s", actualStatus, permittedStatuses);
    }
}
