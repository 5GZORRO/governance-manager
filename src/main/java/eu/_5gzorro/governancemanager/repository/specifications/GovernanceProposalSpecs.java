package eu._5gzorro.governancemanager.repository.specifications;

import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class GovernanceProposalSpecs {
    public static Specification<GovernanceProposal> statusIn(List<GovernanceProposalStatus> statuses) {
        return (proposal, ec, cb) -> {

            if(statuses == null || statuses.size() == 0) {
                return cb.and();
            }

            return proposal.get("status").in(statuses);
        };
    }

    public static Specification<GovernanceProposal> actionTypeIn(List<GovernanceActionType> actionTypes) {
        return (proposal, ec, cb) -> {

            if(actionTypes == null || actionTypes.size() == 0) {
                return cb.and();
            }

            return proposal.get("actionType").in(actionTypes);
        };
    }

    public static Specification<GovernanceProposal> orderByCreated() {
        return (proposal, ec, cb) ->  {
            ec.orderBy(cb.asc(proposal.get("created")));
            return cb.and();
        };
    }
}
