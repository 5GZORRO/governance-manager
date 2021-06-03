package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
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
import java.util.Collections;
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
    private LegalProseRepositoryApiClient lprClientService;

    @Autowired
    private GovernanceProposalRepository governanceProposalRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private GovernanceService governanceService;

    @Value("${callbacks.updateProposalIdentity}")
    private String updateProposalIdentityCallbackUrl;

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

        try {
            String callbackUrl = String.format(updateProposalIdentityCallbackUrl, proposalIdentifier);
            CreateDidRequest didRequest = new CreateDidRequest()
                    .callbackUrl(callbackUrl)
                    .claims(Collections.emptyList())
                    .type(CredentialRequestType.GovernanceProposal);
            identityClientService.createDID(didRequest);
        }
        catch (Exception ex) {
            throw new DIDCreationException(ex);
        }

        proposal.setId(proposalIdentifier);
        governanceProposalRepository.save(proposal);

        return proposalIdentifier;
    }

    @Override
    public UUID processIssueCredentialRequest(IssueCredentialRequest request) throws JsonProcessingException {

        //GovernanceProposal proposal = GovernanceProposalMapper.fromIssueCredentialRequest(authData.getUserId(), request);

        // TODO: Remove when start using proposals
        identityClientService.issueCredential(request);

        return uuidSource.newUUID(); // TODO: replace with proposal creation when ID&P ready
        //return processGovernanceProposal(proposal);
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
            log.error(String.format("Attempted to vote on a proposal not in PROPOSED state with id %s.  Actual state: %s", did, proposal.getStatus()));
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.PROPOSED, proposal.getStatus());
        }

        /**
         * TODO: Create service that performs initial version of voting business logic that encompasses:
         */
        // TODO: Retrieve admin board DID Doc
        // TODO: Implement Vote (creation of VC)
        // TODO: Determine if any more votes needed

        boolean votingThresholdMet = true;

        if(!votingThresholdMet)
            return;

        // TODO: Determine result
        boolean upheld = true;

        completeVotingForProposal(votingStakeholderDid, proposal, upheld);
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

        final boolean canIssueCredential = governanceService.canIssueCredential(proposal);

        if(canIssueCredential) {
            //governanceService.issueCredential(proposal);  // Don't issue proposal credential for now as we need to deal with voting etx. SEE VOTING BRANCH FOR WIP
            proposal.setStatus(GovernanceProposalStatus.APPROVED);

            //Temporary -> issue command to LPR to approve template requests
            // This should be refactored to occur when voting has been completed
            if(proposal.getActionType() == GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE) {
                lprClientService.setTemplateApprovalStatus(proposal.getSubjectId(), true); // always approving currently
            }
        }
        else {
            proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        }

        governanceProposalRepository.save(proposal);
    }

    private void completeVotingForProposal(String votingStakeholderDid, GovernanceProposal proposal, boolean upheld) {

        // TODO: Issue VC with result approved/rejected

        GovernanceProposalStatus status = upheld
                ? GovernanceProposalStatus.APPROVED
                : GovernanceProposalStatus.REJECTED;

        proposal.setStatus(status);
        governanceProposalRepository.save(proposal);

        // TODO: emit event for subscribing entities to deal with
        //  e.g. MembersService to update member status when on-boarding/revocation decision has been reached
    }
}
