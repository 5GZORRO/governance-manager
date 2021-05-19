package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.model.exception.DIDCreationException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalNotFoundException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalStatusException;
import eu._5gzorro.governancemanager.model.exception.InvalidGovernanceActionException;
import eu._5gzorro.governancemanager.model.mapper.GovernanceProposalMapper;
import eu._5gzorro.governancemanager.repository.GovernanceProposalRepository;
import eu._5gzorro.governancemanager.repository.specifications.GovernanceProposalSpecs;
import eu._5gzorro.governancemanager.utils.UuidSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class GovernanceProposalServiceImpl implements GovernanceProposalService {

    private static final Logger log = LogManager.getLogger(GovernanceProposalServiceImpl.class);

    @Autowired
    private UuidSource uuidSource;

    @Autowired
    private IdentityAndPermissionsApiClient identityClientService;

    @Autowired
    private GovernanceProposalRepository governanceProposalRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthData authData;

    @Autowired
    private CredentialManager credentialManager;

    @Value("${callbacks.updateProposalIdentity}")
    private String updateProposalIdentityCallbackUrl;

    @Autowired
    private ProposalVotingManager votingManager;

    private final String myAuthToken = "Token";

    @Override
    @Transactional
    public UUID processGovernanceProposal(String requestingStakeholderDid, ProposeGovernanceDecisionRequest request) {

        if(request.getActionType() == GovernanceActionType.REVOKE_STAKEHOLDER_MEMBERSHIP || request.getActionType() == GovernanceActionType.ONBOARD_STAKEHOLDER) {
            throw new InvalidGovernanceActionException(request.getActionType());
        }

        GovernanceProposal proposal = GovernanceProposalMapper.fromProposeGovernanceDecisionRequest(requestingStakeholderDid, request);
        return processGovernanceProposal(proposal);
    }

    @Override
    @Transactional
    public UUID processGovernanceProposal(GovernanceProposal proposal) {

        if(proposal.getStatus() != GovernanceProposalStatus.CREATING) {
            log.error(String.format("Attempted to process a proposal not in CREATING state. Actual was: %s", proposal.getStatus()));
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.CREATING, proposal.getStatus());
        }

        List<GovernanceProposal> existingProposals = governanceProposalRepository.findBySubjectIdAndActionTypeAndStatusIn(proposal.getSubjectId(), proposal.getActionType(), List.of(GovernanceProposalStatus.CREATING, GovernanceProposalStatus.PROPOSED));

        if(!existingProposals.isEmpty()) {
            throw new IllegalStateException("A proposal of this nature is already being processed for this subject.");
        }

        UUID proposalIdentifier = uuidSource.newUUID();
        String callbackUrl = String.format(updateProposalIdentityCallbackUrl, proposalIdentifier);

        CreateDidRequest didRequest = new CreateDidRequest()
                .type(CredentialRequestType.GOVERNANCE_PROPOSAL)
                .authToken(myAuthToken)
                .callbackUrl(callbackUrl);

        identityClientService.createDID(didRequest);

        proposal.setId(proposalIdentifier);
        governanceProposalRepository.save(proposal);

        return proposalIdentifier;
    }

    @Override
    public Page<GovernanceProposalDto> getGovernanceProposals(Pageable pageable, List<GovernanceActionType> actionTypes, List<GovernanceProposalStatus> statuses) {

        Specification spec = Specification
                .where(GovernanceProposalSpecs.actionTypeIn(actionTypes))
                .and(GovernanceProposalSpecs.statusIn(statuses))
                .and(GovernanceProposalSpecs.orderByCreated());

        Page<GovernanceProposal> pagedEntities = governanceProposalRepository.findAll(spec, pageable);
        Page<GovernanceProposalDto> pagedDtos = pagedEntities.map(proposal -> GovernanceProposalMapper.toGovernanceProposalDto(proposal));

        return pagedDtos;
    }

    @Override
    public GovernanceProposalDto getGovernanceProposalByDid(String did) {

        GovernanceProposal proposal = governanceProposalRepository.findByDid(did)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(did));

        return GovernanceProposalMapper.toGovernanceProposalDto(proposal);
    }

    @Override
    public GovernanceProposalDto getGovernanceProposalById(UUID id) {

        GovernanceProposal proposal = governanceProposalRepository.findById(id)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(id.toString()));

        return GovernanceProposalMapper.toGovernanceProposalDto(proposal);
    }

    @Override
    @Transactional
    public void voteOnGovernanceProposal(String votingStakeholderDid, String did, boolean accept) {

        GovernanceProposal proposal = governanceProposalRepository.findByDid(did)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(did));

        if(proposal.getStatus() != GovernanceProposalStatus.PROPOSED) {
            log.error(String.format("Attempted to vote on a proposal not in PROPOSED state with id %s.  Actual state: %s", proposal.getDid(), proposal.getStatus()));
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.PROPOSED, proposal.getStatus());
        }

        votingManager.castVote(votingStakeholderDid, proposal, accept);
    }

    @Override
    @Transactional
    public void completeGovernanceProposalCreation(UUID id, DIDStateDto state) throws IOException {

        GovernanceProposal proposal = governanceProposalRepository.findById(id)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(id.toString()));

        if(proposal.getStatus() != GovernanceProposalStatus.CREATING)
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.CREATING, proposal.getStatus());

        proposal.setDid(state.getDid());
        proposal.setUpdated(LocalDateTime.now());
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);

        governanceProposalRepository.save(proposal);
    }

    @Override
    @Transactional
    public void updateGovernanceProposal(GovernanceProposal proposal) {
        governanceProposalRepository.save(proposal);
    }
}
