package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.enumeration.DIDStateEnum;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.exception.MemberStatusException;
import eu._5gzorro.governancemanager.model.mapper.GovernanceProposalMapper;
import eu._5gzorro.governancemanager.model.mapper.MemberMapper;
import eu._5gzorro.governancemanager.model.mapper.MemberNotificationSettingsMapper;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;
import eu._5gzorro.governancemanager.model.exception.MemberNotFoundException;
import eu._5gzorro.governancemanager.repository.MemberRepository;
import eu._5gzorro.governancemanager.utils.UuidSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang.SerializationUtils;

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger log = LogManager.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GovernanceProposalService governanceProposalService;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private AuthData authData;

    @Autowired
    private IdentityAndPermissionsApiClient identityAndPermissionsApiClient;

    @Autowired
    private UuidSource uuidSource;


    @Override
    @Transactional
    public UUID processMembershipApplication(RegisterStakeholderRequest request) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        Member member = new Member(request.getStakeholderClaim().getDid(), request.getStakeholderClaim().getStakeholderProfile().getName());
        member.setAddress(request.getStakeholderClaim().getStakeholderProfile().getAddress());
        member.setMembershipRequest(objectMapper.writeValueAsBytes(request));

        //TODO: reinstate when ID&P have implemented it
        //member.addNotificationSettings(MemberNotificationSettingsMapper.toMemberNotificationSettings(request.getNotificationMethod()));

        //TODO: Remove this when proposals reinstated
        member.setStatus(MembershipStatus.ACTIVE);

        memberRepository.save(member);

        // TODO: reinstate once we have completed intial simple integration or "immediately issue "
//        GovernanceProposal proposal = GovernanceProposalMapper.fromNewMembershipRequest(authData.getUserId(), request);
//        UUID proposalIdentifier = governanceProposalService.processGovernanceProposal(proposal);
//        return proposalIdentifier;

        //TODO: Remove this once we start using proposal functionality
        try {
            identityAndPermissionsApiClient.issueStakeholderCredential(request);
        }
        catch(Exception e) {
            e.printStackTrace();
        }


        return uuidSource.newUUID();  // return the proposal UUID here when reinstated
    }

    @Override
    public Page<MemberDto> getMembers(Pageable pageable, String filterText) {
        Page<Member> pagedEntities = memberRepository.findByLegalNameContainingIgnoreCase(filterText, pageable);
        Page<MemberDto> pagedDtos = pagedEntities.map(m -> mapper.map(m, MemberDto.class));
        return pagedDtos;
    }

    @Override
    public MembershipStatusDto getMemberStatus(String id) {
        Member member =  memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        return MemberMapper.toMembershipStatusDto(member);
    }

    @Override
    @Transactional
    public Optional<UUID> revokeMembership(String requestingStakeholderId, String subjectId) {

        Member member = memberRepository.findById(subjectId)
                .orElseThrow(() -> new MemberNotFoundException(subjectId));

        if(member.getStatus() == MembershipStatus.REVOKED)
            return Optional.empty(); //ignore request if already revoked

        if(member.getStatus() != MembershipStatus.ACTIVE) {
            throw new MemberStatusException(MembershipStatus.ACTIVE, member.getStatus());
        }

        // If revoking own membership, no governance process needed
        if(requestingStakeholderId.equals(subjectId)) {
            revokeMembership(member);
            return Optional.empty();
        }

        /**
         * For all other scenarios we need to create a governance proposal
         */
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setProposerId(requestingStakeholderId);
        proposal.setActionType(GovernanceActionType.REVOKE_STAKEHOLDER_MEMBERSHIP);
        proposal.setSubjectId(subjectId);

        UUID proposalIdentifier = governanceProposalService.processGovernanceProposal(proposal);
        return Optional.of(proposalIdentifier);
    }

    private void revokeMembership(Member member) {
        member.setStatus(MembershipStatus.REVOKED);
        member.setUpdated(LocalDateTime.now());
        memberRepository.save(member);
    }
}
