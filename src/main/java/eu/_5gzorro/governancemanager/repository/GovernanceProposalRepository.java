package eu._5gzorro.governancemanager.repository;

import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.List;

public interface GovernanceProposalRepository extends PagingAndSortingRepository<GovernanceProposal, String>, JpaSpecificationExecutor<GovernanceProposal> {

    List<GovernanceProposal> findBySubjectIdAndActionTypeAndStatusIn(String subjectId, GovernanceActionType actionType, Collection<GovernanceProposalStatus> statuses);
}