package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.mapper.MemberMapper;
import eu._5gzorro.governancemanager.model.mapper.MemberNotificationSettingsMapper;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;
import eu._5gzorro.governancemanager.model.exception.MemberNotFoundException;
import eu._5gzorro.governancemanager.repository.MemberRepository;
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

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    @Transactional
    public String processMembershipApplication(NewMembershipRequest request) {

        // TODO: Load DID doc for address & legal name

        Member member = new Member(request.getStakeholderId(), request.getLegalName());
        member.setAddress("");
        member.setStatus(MembershipStatus.PENDING);
        member.addNotificationSettings(MemberNotificationSettingsMapper.toMemberNotificationSettings(request.getNotificationMethod()));
        memberRepository.save(member);

        //TODO: CREATE GOVERNANCE PROPOSAL

        return "PROPOSAL DID";
    }

    @Override
    public Page<MemberDto> getMembers(Pageable pageable, String filterText) {
        Page<Member> pagedEntities = memberRepository.findByLegalNameContainingIgnoreCase(filterText, pageable);
        Page<MemberDto> pagedDtos = pagedEntities.map(m -> mapper.map(m, MemberDto.class));
        return pagedDtos;
    }

    @Override
    public MembershipStatusDto getMemberStatus(String id) {
        Optional<Member> member =  memberRepository.findById(id);

        if(member.isEmpty())
            throw new MemberNotFoundException(id);

        return MemberMapper.toMembershipStatusDto(member.get());
    }

    @Override
    @Transactional
    public void processMembershipRevocationRequest(String requestingStakeholderId, String subjectId) {

        Member member = memberRepository.findById(subjectId)
                .orElseThrow(() -> new MemberNotFoundException(subjectId));

        if(requestingStakeholderId.equals(subjectId)) {
            revokeMembership(member);
            return;
        }

        // TODO: Create governance proposal
    }

    @Override
    @Transactional
    public void revokeMembership(String subjectId) {
        Member member = memberRepository.findById(subjectId)
                .orElseThrow(() -> new MemberNotFoundException(subjectId));

        revokeMembership(member);
    }

    @Override
    @Transactional
    public void revokeMembership(Member member) {
        member.setStatus(MembershipStatus.REVOKED);
        member.setUpdated(LocalDateTime.now());
        memberRepository.save(member);
    }
}
