package eu._5gzorro.governancemanager.memberTests;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.config.Config;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.dto.EmailNotificationDto;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.ClaimDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.StakeholderClaimDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.StakeholderProfileDto;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.entity.MemberNotificationSetting;
import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;
import eu._5gzorro.governancemanager.model.enumeration.NotificationSetting;
import eu._5gzorro.governancemanager.model.enumeration.NotificationType;
import eu._5gzorro.governancemanager.model.exception.MemberNotFoundException;
import eu._5gzorro.governancemanager.model.exception.MemberStatusException;
import eu._5gzorro.governancemanager.repository.MemberRepository;
import eu._5gzorro.governancemanager.service.GovernanceProposalService;
import eu._5gzorro.governancemanager.service.IdentityAndPermissionsApiClient;
import eu._5gzorro.governancemanager.service.MemberService;
import eu._5gzorro.governancemanager.service.MemberServiceImpl;
import eu._5gzorro.governancemanager.utils.UuidSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {MemberServiceImplUnitTest.MemberServiceImplUnitTestContextConfiguration.class })
class MemberServiceImplUnitTest {

    @TestConfiguration
    static class MemberServiceImplUnitTestContextConfiguration extends Config {
        @Bean
        public MemberService memberService() {
            return new MemberServiceImpl();
        }
    }

    @Autowired
    private MemberService memberService;

    @MockBean
    GovernanceProposalService governanceProposalService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private IdentityAndPermissionsApiClient identityAndPermissionsApiClient;

    @MockBean
    private UuidSource uuidSource;

    @MockBean
    private AuthData authData;

    @Test
    void whenFindByLegalNameMatches_thenReturnMatchingMemberDtos() {
        MemberDto expectedMemberDto = new MemberDto();
        expectedMemberDto.setLegalName("Telefonica");
        expectedMemberDto.setStakeholderId("1");

        // when
        Member member = new Member("1", "Telefonica");
        member.setStatus(MembershipStatus.PENDING);
        Page expectedMemberPage = new PageImpl(List.of(member));

        String nameFilter = "Tele";
        Pageable page = PageRequest.of(0, 10);
        Mockito.when(memberRepository.findByLegalNameContainingIgnoreCase(nameFilter, page)).thenReturn(expectedMemberPage);

        Page<MemberDto> result = memberService.getMembers(page, nameFilter);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(expectedMemberDto, result.getContent().get(0));
    }

    @Test
    void processMembershipApplication_returnsProposalId() throws JsonProcessingException {

        UUID mockedProposalHandle = UUID.randomUUID();
        String mockedStakeholderDid = "did:5gzorro:1234567890";
        Member expectedMember = new Member(mockedStakeholderDid, "Telefonica");
        expectedMember.setStatus(MembershipStatus.PENDING);

        MemberNotificationSetting setting = new MemberNotificationSetting()
                .notificationType(NotificationType.EMAIL)
                .setting(NotificationSetting.EMAIL_DISTRIBUTION_LIST)
                .value("person@mail.com");

        expectedMember.addNotificationSetting(setting);

        // given
        EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setDistributionList(List.of("person@mail.com"));

        StakeholderProfileDto profile = new StakeholderProfileDto();
        profile.setName("Telefonica");
        profile.setAddress("10 the Street");
        profile.setNotificationMethod(notificationDto);

        StakeholderClaimDto claim = new StakeholderClaimDto();
        claim.setDid(mockedStakeholderDid);
        claim.setGovernanceBoardId("did:5gzorro:12345678901");
        claim.setStakeholderProfile(profile);

        RegisterStakeholderRequest request = new RegisterStakeholderRequest();
        request.setStakeholderClaim(claim);

        // when
        // TODO: Remove this when reinstating proposals
        Mockito.when(uuidSource.newUUID()).thenReturn(mockedProposalHandle);

        Mockito.when(governanceProposalService.processGovernanceProposal(any(GovernanceProposal.class))).thenReturn(mockedProposalHandle);
        UUID result = memberService.processMembershipApplication(request);

        // then
        verify(memberRepository, times(1)).save(expectedMember);
        assertEquals(mockedProposalHandle, result);
    }

    @Test
    void getMemberStatus_returnsMemberStatus() {

        final String stakeholderId = "1";
        MembershipStatusDto expectedMember = new MembershipStatusDto();
        expectedMember.setStakeholderId(stakeholderId);
        expectedMember.setStatus(MembershipStatus.PENDING);

        // given
        Member member = new Member(stakeholderId, "Telefonica");
        member.setStatus(MembershipStatus.PENDING);

        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.of(member));

        // when
        MembershipStatusDto result = memberService.getMemberStatus(stakeholderId);

        // then
        verify(memberRepository, times(1)).findById(stakeholderId);
        assertEquals(expectedMember, result);
    }

    @Test
    void getMemberStatus_throwsMemberNotFoundExceptionWhenMemberNotFound() {

        // given
        final String stakeholderId = "1";
        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(MemberNotFoundException.class, () -> memberService.getMemberStatus(stakeholderId));
    }

    @Test
    void revokeMembership_throwsMemberNotFoundExceptionWhenMemberNotFound() {

        // given
        final String requestingStakeholderId = "2";
        final String stakeholderId = "1";
        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(MemberNotFoundException.class, () -> memberService.revokeMembership(requestingStakeholderId, stakeholderId));
    }

    @Test
    void processMembershipRevocationRequest_throwsMemberNotFoundExceptionWhenMemberNotFound() {

        // given
        final String requestingStakeholderId = "2";
        final String stakeholderId = "1";
        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(MemberNotFoundException.class, () -> memberService.revokeMembership(requestingStakeholderId, stakeholderId));
    }

    @Test
    void processMembershipRevocationRequest_setsMembershipStatusToRevokedWhenStakeholderAndRequesterTheSameAndNoProposalGenerated() {

        // given
        final String requestingStakeholderId = "1";
        final String stakeholderId = "1";

        Member member = new Member(stakeholderId, "Telefonica");
        member.setStatus(MembershipStatus.ACTIVE);

        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.of(member));

        Optional<UUID> generatedProposalId = memberService.revokeMembership(requestingStakeholderId, stakeholderId);

        // The expected updated state of the member after calling revoke
        Member updatedMember = new Member(stakeholderId, "Telefonica");
        member.setStatus(MembershipStatus.REVOKED);

        // then
        assertTrue(generatedProposalId.isEmpty());
        verify(memberRepository, times(1)).save(updatedMember);
    }

    @Test
    void processMembershipRevocationRequest_throwsMemberStatusExceptionWhenMemberNotActive() {

        // given
        final String requestingStakeholderId = "2";
        final String stakeholderId = "1";
        Member member = new Member(stakeholderId, "Telefonica");
        member.setStatus(MembershipStatus.PENDING);

        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.of(member));

        // then
        Throwable exception = assertThrows(MemberStatusException.class, () -> memberService.revokeMembership(requestingStakeholderId, stakeholderId));
    }

    @Test
    void processMembershipRevocationRequest_doesNotSetMembershipStatusToRevokedWhenStakeholderAndRequesterDifferentButDoesCreateProposal() {

        // given
        final UUID mockedProposalId = UUID.randomUUID();
        final String requestingStakeholderId = "2";
        final String stakeholderId = "1";

        Member member = new Member(stakeholderId, "Telefonica");
        member.setStatus(MembershipStatus.ACTIVE);

        when(memberRepository.findById(stakeholderId)).thenReturn(Optional.of(member));
        when(governanceProposalService.processGovernanceProposal(any(GovernanceProposal.class))).thenReturn(mockedProposalId);

        // Execute
        Optional<UUID> generatedProposalIdentifier = memberService.revokeMembership(requestingStakeholderId, stakeholderId);

        assertEquals(mockedProposalId, generatedProposalIdentifier.get());
        verify(memberRepository, never()).save(any(Member.class));
        verify(governanceProposalService, times(1)).processGovernanceProposal(any(GovernanceProposal.class));
    }
}
