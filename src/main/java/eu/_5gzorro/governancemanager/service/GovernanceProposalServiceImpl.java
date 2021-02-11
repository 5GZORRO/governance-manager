package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalNotFoundException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalStatusException;
import eu._5gzorro.governancemanager.model.exception.InvalidGovernanceActionException;
import eu._5gzorro.governancemanager.model.exception.MemberNotFoundException;
import eu._5gzorro.governancemanager.model.mapper.GovernanceProposalMapper;
import eu._5gzorro.governancemanager.model.mapper.MemberMapper;
import eu._5gzorro.governancemanager.model.mapper.MemberNotificationSettingsMapper;
import eu._5gzorro.governancemanager.repository.GovernanceProposalRepository;
import eu._5gzorro.governancemanager.repository.MemberRepository;
import eu._5gzorro.governancemanager.repository.specifications.GovernanceProposalSpecs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GovernanceProposalServiceImpl implements GovernanceProposalService {

    private static final Logger log = LogManager.getLogger(GovernanceProposalServiceImpl.class);

    @Autowired
    private GovernanceProposalRepository governanceProposalRepository;

    @Autowired
    private ModelMapper mapper;


    @Override
    @Transactional
    public String processGovernanceProposal(String requestingStakeholderId, ProposeGovernanceDecisionRequest request) {

        if(request.getActionType() == GovernanceActionType.REVOKE_STAKEHOLDER_MEMBERSHIP || request.getActionType() == GovernanceActionType.ONBOARD_STAKEHOLDER) {
            throw new InvalidGovernanceActionException(request.getActionType());
        }

        GovernanceProposal proposal = GovernanceProposalMapper.fromProposeGovernanceDecisionRequest(requestingStakeholderId, request);
        return processGovernanceProposal(proposal);
    }

    @Override
    @Transactional
    public String processGovernanceProposal(GovernanceProposal proposal) {

        if(proposal.getStatus() != GovernanceProposalStatus.PROPOSED) {
            log.error(String.format("Attempted to process a proposal not in PROPOSED state. Actual was: %s", proposal.getStatus()));
            throw new GovernanceProposalStatusException(GovernanceProposalStatus.PROPOSED, proposal.getStatus());
        }

        List<GovernanceProposal> existingProposals = governanceProposalRepository.findBySubjectIdAndActionTypeAndStatusIn(proposal.getSubjectId(), proposal.getActionType(), List.of(GovernanceProposalStatus.PROPOSED));

        if(existingProposals.size() > 0) {
            throw new IllegalStateException("A proposal of this nature is already being processed for this subject.");
        }


        // TODO: Generate a DID for the proposal
        String proposalIdentifier = "DID";

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
    public GovernanceProposalDto getGovernanceProposal(String id) {

        GovernanceProposal proposal = governanceProposalRepository.findById(id)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(id));

        return GovernanceProposalMapper.toGovernanceProposalDto(proposal);
    }

    @Override
    @Transactional
    public void voteOnGovernanceProposal(String votingStakeholderId, String id, boolean accept) {

        GovernanceProposal proposal = governanceProposalRepository.findById(id)
                .orElseThrow(() -> new GovernanceProposalNotFoundException(id));

        if(proposal.getStatus() != GovernanceProposalStatus.PROPOSED) {
            log.error(String.format("Attempted to vote on a proposal not in PROPOSED state with id %s.  Actual state: %s", id, proposal.getStatus()));
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

        completeVotingForProposal(votingStakeholderId, proposal, upheld);
    }

    private void completeVotingForProposal(String votingStakeholderId, GovernanceProposal proposal, boolean upheld) {

        // TODO: Issue VC with result approved/rejected

        GovernanceProposalStatus status = upheld
                ? GovernanceProposalStatus.APPROVED
                : GovernanceProposalStatus.REJECTED;

        proposal.setStatus(status);
        governanceProposalRepository.save(proposal);

        // TODO: emit event for subscribing entities to deal with
        //  e.g. MembersService to update member status when on-boarding decision has been reached
    }
}
