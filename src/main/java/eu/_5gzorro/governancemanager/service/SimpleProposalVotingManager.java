package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposalVote;
import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.model.exception.CredentialIssuanceException;
import eu._5gzorro.governancemanager.model.exception.DIDCreationException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalStatusException;
import eu._5gzorro.governancemanager.repository.GovernanceProposalVoteRepository;
import eu._5gzorro.governancemanager.utils.UuidSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.UUID;

@Service
public class SimpleProposalVotingManager implements ProposalVotingManager {

    private static final Logger log = LogManager.getLogger(SimpleProposalVotingManager.class);

    @Autowired
    GovernanceProposalService proposalService;

    @Autowired
    GovernanceProposalVoteRepository voteRepository;

    @Autowired
    IdentityAndPermissionsApiClient identityClient;

    @Autowired
    private UuidSource uuidSource;

    @Override
    @Transactional
    public void castVote(String votingStakeholderDid, GovernanceProposal proposal, boolean accept) {

        if(proposal.getStatus() != GovernanceProposalStatus.PROPOSED) {
            log.error(String.format("Attempted to vote on a proposal not in PROPOSED state with id %s.  Actual state: %s", proposal.getDid(), proposal.getStatus()));
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.PROPOSED, proposal.getStatus());
        }

        // TODO: add appropriate claims

        String callbackUrl = "";
        CreateDidRequest didRequest = new CreateDidRequest()
                .callbackUrl(callbackUrl)
                .type(CredentialRequestType.GOVERNANCE_VOTE);

        identityClient.createDID(didRequest, proposal.getProposerId());

        GovernanceProposalVote vote = new GovernanceProposalVote();
        vote.setId(uuidSource.newUUID());
        vote.setApproved(accept);
        vote.setVoterDid(votingStakeholderDid);
        proposal.addVote(vote);

        proposalService.updateGovernanceProposal(proposal);
    }

    @Override
    public void completeVoteCreation(UUID proposalId, UUID voteId, DIDStateDto state) {
     //   voteRepository.findById(voteId).orElseThrow(new )
    }

    private void completeVotingForProposal(String votingStakeholderDid, GovernanceProposal proposal, boolean upheld) {

//        try {
//            credentialManager.issueCredential(proposal); //TODO -> create VOTING DECISION credential, not issue a proposal
//        }
//        catch (Exception ex) {
//            log.error(ex);
//            throw new CredentialIssuanceException(ex);
//        }
//
//        GovernanceProposalStatus status = upheld
//                ? GovernanceProposalStatus.APPROVED
//                : GovernanceProposalStatus.REJECTED;
//
//        proposal.setStatus(status);
//        proposalService.updateGovernanceProposal(proposal);
//
//        notifySubscribingAppsOfGovernanceDecision();

        // TODO: emit event for subscribing entities to deal with
        //  e.g. MembersService to update member status when on-boarding/revocation decision has been reached
    }

    private void notifySubscribingAppsOfGovernanceDecision() {

    }
}
