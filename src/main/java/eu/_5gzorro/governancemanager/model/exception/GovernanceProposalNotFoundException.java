package eu._5gzorro.governancemanager.model.exception;

import javax.persistence.EntityNotFoundException;

public class GovernanceProposalNotFoundException extends EntityNotFoundException {

    private final String id;

    public GovernanceProposalNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Governance Proposal with id '%s' not found", id);
    }
}
