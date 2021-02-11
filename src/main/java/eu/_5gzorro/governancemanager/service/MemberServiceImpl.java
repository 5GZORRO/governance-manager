package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.GovernanceManagerApplication;
import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
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

@Service
public class MemberServiceImpl implements MemberService {

    private static final Logger log = LogManager.getLogger(MemberServiceImpl.class);

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private GovernanceProposalService governanceProposalService;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public String processMembershipApplication(NewMembershipRequest request) {

        // TODO: Load DID doc or VC for address & legal name
        String legalName = request.getLegalName();  //get from request for now (should be from DD/VC)
        String address = "";

        Member member = new Member(request.getStakeholderId(), legalName);
        member.setAddress(address);
        member.addNotificationSettings(MemberNotificationSettingsMapper.toMemberNotificationSettings(request.getNotificationMethod()));
        memberRepository.save(member);

        GovernanceProposal proposal = GovernanceProposalMapper.fromNewMembershipRequest(request);
        String proposalIdentifier = governanceProposalService.processGovernanceProposal(proposal);

        return proposalIdentifier;
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
    public Optional<String> revokeMembership(String requestingStakeholderId, String subjectId) {

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

        String proposalIdentifier = governanceProposalService.processGovernanceProposal(proposal);
        return Optional.of(proposalIdentifier);
    }

//    @Override
//    @Transactional
//    public void revokeMembership(String subjectId) {
//        Member member = memberRepository.findById(subjectId)
//                .orElseThrow(() -> new MemberNotFoundException(subjectId));
//
//        revokeMembership(member);
//    }

    private void revokeMembership(Member member) {
        member.setStatus(MembershipStatus.REVOKED);
        member.setUpdated(LocalDateTime.now());
        memberRepository.save(member);
    }
}
