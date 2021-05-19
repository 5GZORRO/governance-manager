package eu._5gzorro.governancemanager.repository;

import eu._5gzorro.governancemanager.model.entity.GovernanceProposalVote;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface GovernanceProposalVoteRepository extends PagingAndSortingRepository<GovernanceProposalVote, UUID> {
    Optional<GovernanceProposalVote> findByDid(String did);
    boolean existsByDid(String did);
    void deleteByDid(String did);
}