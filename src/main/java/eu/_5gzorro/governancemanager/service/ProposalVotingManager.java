package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;

import java.util.UUID;

public interface ProposalVotingManager {
    void castVote(String votingStakeholderDid, GovernanceProposal proposal, boolean accept);
    void completeVoteCreation(UUID proposalId, UUID voteId, DIDStateDto state);
}
