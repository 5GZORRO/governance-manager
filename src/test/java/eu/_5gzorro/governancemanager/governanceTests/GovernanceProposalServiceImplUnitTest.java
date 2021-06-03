package eu._5gzorro.governancemanager.governanceTests;

import eu._5gzorro.governancemanager.config.Config;
import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.ActionParamsDto;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalNotFoundException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalStatusException;
import eu._5gzorro.governancemanager.repository.GovernanceProposalRepository;
import eu._5gzorro.governancemanager.service.*;
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
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {GovernanceProposalServiceImplUnitTest.GovernanceProposalServiceImplUnitTestContextConfiguration.class })
@TestPropertySource(properties = { "callbacks.updateProposalIdentity=http://localhost:8080/api/v1/governance-actions/%s/identity" })
public class GovernanceProposalServiceImplUnitTest {

    @TestConfiguration
    static class GovernanceProposalServiceImplUnitTestContextConfiguration extends Config {
        @Bean
        public GovernanceProposalService governanceProposalService() {
            return new GovernanceProposalServiceImpl();
        }
    }

    @Autowired
    private GovernanceProposalService governanceProposalService;

    @MockBean
    private GovernanceService governanceService;

    @MockBean
    private GovernanceProposalRepository governanceProposalRepository;

    @MockBean
    private IdentityAndPermissionsApiClient identityClientService;

    @MockBean
    private LegalProseRepositoryApiClient lprClientService;

    @MockBean
    private UuidSource uuidSource;

    @Test
    public void processGovernanceProposal_returnsProposalId() {

        String stakeholderId = "stakeholderDID";
        String subjectId = "subjectDID";
        UUID expectedId = UUID.randomUUID();
        String mockedAuthToken = "TOKEN";

        GovernanceProposal expectedProposal = new GovernanceProposal();
        expectedProposal.setId(expectedId);
        expectedProposal.setStatus(GovernanceProposalStatus.CREATING);
        expectedProposal.setProposerId(stakeholderId);
        expectedProposal.setSubjectId(subjectId);
        expectedProposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        ProposeGovernanceDecisionRequest request = new ProposeGovernanceDecisionRequest();
        request.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);


        ActionParamsDto actionParams = new ActionParamsDto();
        actionParams.setEntityIdentityId(subjectId);
        request.setActionParams(actionParams);

        // when
        Mockito.when(uuidSource.newUUID()).thenReturn(expectedId);

        UUID result = governanceProposalService.processGovernanceProposal(stakeholderId, request);

        // then
        String expectedCalbackUrl = String.format("http://localhost:8080/api/v1/governance-actions/%s/identity", expectedId);
        CreateDidRequest didRequest = new CreateDidRequest()
                .callbackUrl(expectedCalbackUrl)
                .claims(Collections.emptyList())
                .type(CredentialRequestType.GovernanceProposal);

        verify(governanceProposalRepository, times(1)).save(expectedProposal);
        verify(identityClientService, times(1)).createDID(didRequest);
        assertEquals(expectedId, result);
    }

    @Test
    public void processGovernanceProposal_methodOverload_returnsProposalId() {

        String stakeholderId = "stakeholderDID";
        String subjectId = "subjectDID";
        UUID expectedId = UUID.randomUUID();
        String mockedAuthToken = "TOKEN";

        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setStatus(GovernanceProposalStatus.CREATING);
        proposal.setProposerId(stakeholderId);
        proposal.setSubjectId(subjectId);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // when
        Mockito.when(uuidSource.newUUID()).thenReturn(expectedId);

        UUID result = governanceProposalService.processGovernanceProposal(proposal);

        // then
        verify(governanceProposalRepository, times(1)).save(proposal);

        String expectedCalbackUrl = String.format("http://localhost:8080/api/v1/governance-actions/%s/identity", expectedId);
        CreateDidRequest didRequest = new CreateDidRequest()
                .callbackUrl(expectedCalbackUrl)
                .claims(Collections.emptyList())
                .type(CredentialRequestType.GovernanceProposal);

        verify(identityClientService, times(1)).createDID(didRequest);
        assertEquals(expectedId, result);
    }

    @Test
    public void processGovernanceProposal_throwsGovernanceProposalStatusException_whenProposalNotInProposedState() {

        // given
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(UUID.randomUUID());
        proposal.setDid("did:5gzorro:64654654564");
        proposal.setStatus(GovernanceProposalStatus.APPROVED);
        proposal.setProposerId("stakeholderDID");
        proposal.setSubjectId("subjectDID");
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // then
        Throwable exception = assertThrows(GovernanceProposalStatusException.class, () -> governanceProposalService.processGovernanceProposal(proposal));
    }


    @Test
    public void getGovernanceProposals_returnsMatchingDtos() {
        MemberDto expectedMemberDto = new MemberDto();
        expectedMemberDto.setLegalName("Telefonica");
        expectedMemberDto.setStakeholderId("1");

        // when
        // Entities in db:
        GovernanceProposal proposal1 = new GovernanceProposal();
        proposal1.setId(UUID.randomUUID());
        proposal1.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal1.setProposerId("stakeholder1");
        proposal1.setSubjectId("template1");
        proposal1.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal proposal2 = new GovernanceProposal();
        proposal2.setId(UUID.randomUUID());
        proposal2.setStatus(GovernanceProposalStatus.APPROVED);
        proposal2.setProposerId("stakeholder1");
        proposal2.setSubjectId("template2");
        proposal2.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal proposal3 = new GovernanceProposal();
        proposal3.setId(UUID.randomUUID());
        proposal3.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal3.setProposerId("stakeholder1");
        proposal3.setSubjectId("stakeholder1233");
        proposal3.setActionType(GovernanceActionType.ONBOARD_STAKEHOLDER);

        // Query params
        List<GovernanceActionType> actionFilter = new ArrayList<>();
        List<GovernanceProposalStatus> statusFilter = new ArrayList<>();
        Pageable page = PageRequest.of(0, 10);

        Page<GovernanceProposal> expectedPage = new PageImpl(List.of(proposal1, proposal2, proposal3));
        Mockito.when(governanceProposalRepository.findAll(Mockito.any(Specification.class), Mockito.any(Pageable.class))).thenReturn(expectedPage);

        Page<GovernanceProposalDto> result = governanceProposalService.getGovernanceProposals(page, actionFilter, statusFilter);

        // then
        assertEquals(expectedPage.getSize(), result.getSize());

        for (int i = 0; i < expectedPage.getSize(); i++) {
            assertTrue(expectedPage.getContent().get(i).getId().toString().equals(result.getContent().get(i).getProposalId()));
        }
    }

    @Test
    public void getGovernanceProposalById_returnsMatchingGovernanceProposalDto() {

        final UUID expectedProposalId = UUID.randomUUID();
        GovernanceProposalDto expected = new GovernanceProposalDto();
        expected.setProposalId(expectedProposalId.toString());
        expected.setStatus(GovernanceProposalStatus.PROPOSED);
        expected.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(expectedProposalId);
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findById(expectedProposalId)).thenReturn(Optional.of(proposal));

        // when
        GovernanceProposalDto result = governanceProposalService.getGovernanceProposalById(expectedProposalId);

        // then
        verify(governanceProposalRepository, times(1)).findById(expectedProposalId);
        assertEquals(expected, result);
    }

    @Test
    public void getGovernanceProposalById_throwsGovernanceProposalNotFoundException_whenNotFound() {

        // given
        final UUID expectedProposalId = UUID.randomUUID();
        when(governanceProposalRepository.findById(expectedProposalId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(GovernanceProposalNotFoundException.class, () -> governanceProposalService.getGovernanceProposalById(expectedProposalId));
    }


    @Test
    public void getGovernanceProposaByDid_returnsMatchingGovernanceProposalDto() {

        final UUID expectedProposalId = UUID.randomUUID();
        final String expectedDid = "did:5gzorro:354654621523184";
        GovernanceProposalDto expected = new GovernanceProposalDto();
        expected.setProposalId(expectedDid);
        expected.setStatus(GovernanceProposalStatus.PROPOSED);
        expected.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(expectedProposalId);
        proposal.setDid(expectedDid);
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findByDid(expectedDid)).thenReturn(Optional.of(proposal));

        // when
        GovernanceProposalDto result = governanceProposalService.getGovernanceProposalByDid(expectedDid);

        // then
        verify(governanceProposalRepository, times(1)).findByDid(expectedDid);
        assertEquals(expected, result);
    }

    @Test
    public void getGovernanceProposalByDid_throwsGovernanceProposalNotFoundException_whenNotFound() {

        // given
        final String expectedDid = "did:5gzorro:354654621523184";
        when(governanceProposalRepository.findByDid(expectedDid)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(GovernanceProposalNotFoundException.class, () -> governanceProposalService.getGovernanceProposalByDid(expectedDid));
    }


    @Test
    public void voteOnGovernanceProposal_throwsGovernanceProposalStatusException_whenNotInProposedState() {

        // given
        final UUID proposalId = UUID.randomUUID();
        final String did = "did:5gzorro:354654621523184";
        final String votingStakeholderId = "DID";

        final GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(proposalId);
        proposal.setDid(did);
        proposal.setStatus(GovernanceProposalStatus.APPROVED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findByDid(did)).thenReturn(Optional.of(proposal));

        // then
        Throwable exception = assertThrows(GovernanceProposalStatusException.class, () -> governanceProposalService.voteOnGovernanceProposal(votingStakeholderId, did, true));
    }

    @Test
    public void voteOnGovernanceProposal_successfullyUpdatesProposalStatusWhenLastVotingStakeholderVotes() {

        // given
        final UUID proposalId = UUID.randomUUID();
        final String did = "did:5gzorro:354654621523184";
        final String votingStakeholderId = "DID";

        final GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(proposalId);
        proposal.setDid(did);
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findByDid(did)).thenReturn(Optional.of(proposal));

        governanceProposalService.voteOnGovernanceProposal(votingStakeholderId, did, true);

        // then expect status to have been updated to approved and save called
        final GovernanceProposal updatedProposal = proposal;
        updatedProposal.setStatus(GovernanceProposalStatus.APPROVED);

        verify(governanceProposalRepository, times(1)).save(updatedProposal);
    }
}
