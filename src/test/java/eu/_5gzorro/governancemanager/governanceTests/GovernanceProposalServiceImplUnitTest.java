package eu._5gzorro.governancemanager.governanceTests;

import eu._5gzorro.governancemanager.config.Config;
import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.ActionParamsDto;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalNotFoundException;
import eu._5gzorro.governancemanager.model.exception.GovernanceProposalStatusException;
import eu._5gzorro.governancemanager.repository.GovernanceProposalRepository;
import eu._5gzorro.governancemanager.service.GovernanceProposalService;
import eu._5gzorro.governancemanager.service.GovernanceProposalServiceImpl;
import eu._5gzorro.governancemanager.service.GovernanceService;
import eu._5gzorro.governancemanager.service.IdentityAndPermissionsApiClient;
import eu._5gzorro.governancemanager.utils.UuidSource;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private AuthData authData;

    @MockBean
    private UuidSource uuidSource;

    @Test
    public void processGovernanceProposal_returnsProposalId() {

        String stakeholderId = "stakeholderDID";
        String subjectId = "subjectDID";
        UUID mockedProposalHandle = UUID.randomUUID();
        String mockedAuthToken = "TOKEN";

        GovernanceProposal expectedProposal = new GovernanceProposal();
        expectedProposal.setId(mockedProposalHandle.toString());
        expectedProposal.setStatus(GovernanceProposalStatus.CREATING);
        expectedProposal.setHandle(mockedProposalHandle);
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
        Mockito.when(authData.getAuthToken()).thenReturn(mockedAuthToken);
        Mockito.when(uuidSource.newUUID()).thenReturn(mockedProposalHandle);

        UUID result = governanceProposalService.processGovernanceProposal(stakeholderId, request);

        // then
        String expectedCalbackUrl = String.format("http://localhost:8080/api/v1/governance-actions/%s/identity", mockedProposalHandle);
        verify(governanceProposalRepository, times(1)).save(expectedProposal);
        verify(identityClientService, times(1)).createDID(expectedCalbackUrl, mockedAuthToken);
        assertEquals(mockedProposalHandle, result);
    }

    @Test
    public void processGovernanceProposal_methodOverload_returnsProposalId() {

        String stakeholderId = "stakeholderDID";
        String subjectId = "subjectDID";
        UUID mockedProposalHandle = UUID.randomUUID();
        String mockedAuthToken = "TOKEN";

        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setStatus(GovernanceProposalStatus.CREATING);
        proposal.setProposerId(stakeholderId);
        proposal.setSubjectId(subjectId);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // when
        Mockito.when(authData.getAuthToken()).thenReturn(mockedAuthToken);
        Mockito.when(uuidSource.newUUID()).thenReturn(mockedProposalHandle);

        UUID result = governanceProposalService.processGovernanceProposal(proposal);

        // then
        verify(governanceProposalRepository, times(1)).save(proposal);

        String expectedCalbackUrl = String.format("http://localhost:8080/api/v1/governance-actions/%s/identity", mockedProposalHandle);
        verify(identityClientService, times(1)).createDID(expectedCalbackUrl, mockedAuthToken);
        assertEquals(mockedProposalHandle, result);
    }

    @Test
    public void processGovernanceProposal_throwsGovernanceProposalStatusException_whenProposalNotInProposedState() {

        // given
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId("proposalDID");
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
        proposal1.setId("proposal1");
        proposal1.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal1.setProposerId("stakeholder1");
        proposal1.setSubjectId("template1");
        proposal1.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal proposal2 = new GovernanceProposal();
        proposal2.setId("proposal2");
        proposal2.setStatus(GovernanceProposalStatus.APPROVED);
        proposal2.setProposerId("stakeholder1");
        proposal2.setSubjectId("template2");
        proposal2.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal proposal3 = new GovernanceProposal();
        proposal3.setId("proposal3");
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
            assertEquals(expectedPage.getContent().get(i).getId(), result.getContent().get(i).getProposalId());
        }
    }

    @Test
    public void getGovernanceProposal_returnsMatchingGovernanceProposalDto() {

        final String proposalId = "1";
        GovernanceProposalDto expected = new GovernanceProposalDto();
        expected.setProposalId(proposalId);
        expected.setStatus(GovernanceProposalStatus.PROPOSED);
        expected.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(proposalId);
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        // when
        GovernanceProposalDto result = governanceProposalService.getGovernanceProposal(proposalId);

        // then
        verify(governanceProposalRepository, times(1)).findById(proposalId);
        assertEquals(expected, result);
    }

    @Test
    public void getGovernanceProposal_throwsGovernanceProposalNotFoundException_whenNotFound() {

        // given
        final String proposalId = "1";
        when(governanceProposalRepository.findById(proposalId)).thenReturn(Optional.empty());

        // then
        Throwable exception = assertThrows(GovernanceProposalNotFoundException.class, () -> governanceProposalService.getGovernanceProposal(proposalId));
    }


    @Test
    public void voteOnGovernanceProposal_throwsGovernanceProposalStatusException_whenNotInProposedState() {

        // given
        final String proposalId = "1";
        final String votingStakeholderId = "DID";

        final GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(proposalId);
        proposal.setStatus(GovernanceProposalStatus.APPROVED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        // then
        Throwable exception = assertThrows(GovernanceProposalStatusException.class, () -> governanceProposalService.voteOnGovernanceProposal(votingStakeholderId, proposalId, true));
    }

    @Test
    public void voteOnGovernanceProposal_successfullyUpdatesProposalStatusWhenLastVotingStakeholderVotes() {

        // given
        final String proposalId = "1";
        final String votingStakeholderId = "DID";

        final GovernanceProposal proposal = new GovernanceProposal();
        proposal.setId(proposalId);
        proposal.setStatus(GovernanceProposalStatus.PROPOSED);
        proposal.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        when(governanceProposalRepository.findById(proposalId)).thenReturn(Optional.of(proposal));

        governanceProposalService.voteOnGovernanceProposal(votingStakeholderId, proposalId, true);

        // then expect status to have been updated to approved and save called
        final GovernanceProposal updatedProposal = proposal;
        updatedProposal.setStatus(GovernanceProposalStatus.APPROVED);

        verify(governanceProposalRepository, times(1)).save(updatedProposal);
    }
}
