package eu._5gzorro.governancemanager.governanceTests;

import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceProposalStatus;
import eu._5gzorro.governancemanager.repository.GovernanceProposalRepository;
import eu._5gzorro.governancemanager.repository.specifications.GovernanceProposalSpecs;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class GovernanceProposalRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GovernanceProposalRepository governanceProposalRepository;

    @Test
    public void whenStatusFilterProvided_thenReturnMatchingProposals() {

        GovernanceProposal p1 = new GovernanceProposal();
        p1.setId("1");
        p1.setHandle(UUID.randomUUID());
        p1.setProposerId("proposer_123");
        p1.setSubjectId("subject_123");
        p1.setStatus(GovernanceProposalStatus.PROPOSED);
        p1.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal p2 = new GovernanceProposal();
        p2.setId("3");
        p2.setHandle(UUID.randomUUID());
        p2.setProposerId("proposer_1");
        p2.setSubjectId("subject_1");
        p2.setStatus(GovernanceProposalStatus.APPROVED);
        p2.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        List<GovernanceProposal> proposals = new ArrayList<GovernanceProposal>() {
            {
                add(p1);
                add(p2);
            }
        };

        proposals.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        List<GovernanceActionType> actionsFilter = new ArrayList<>();
        List<GovernanceProposalStatus> statusesFilter = List.of(GovernanceProposalStatus.PROPOSED);

        Pageable page = PageRequest.of(0, 10);
        Page<GovernanceProposal> found = governanceProposalRepository.findAll(GovernanceProposalSpecs.statusIn(statusesFilter), page);

        // then
        assertEquals(1, found.getTotalElements());
        assertEquals(p1, found.getContent().get(0));
    }

    @Test
    public void whenActionTypeFilterProvided_thenReturnMatchingProposals() {

        GovernanceProposal p1 = new GovernanceProposal();
        p1.setId("1");
        p1.setHandle(UUID.randomUUID());
        p1.setProposerId("proposer_123");
        p1.setSubjectId("subject_123");
        p1.setStatus(GovernanceProposalStatus.PROPOSED);
        p1.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal p2 = new GovernanceProposal();
        p2.setId("3");
        p2.setHandle(UUID.randomUUID());
        p2.setProposerId("proposer_1");
        p2.setSubjectId("subject_1");
        p2.setStatus(GovernanceProposalStatus.APPROVED);
        p2.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        // given
        List<GovernanceProposal> proposals = new ArrayList<GovernanceProposal>() {
            {
                add(p1);
                add(p2);
            }
        };

        proposals.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        List<GovernanceActionType> actionsFilter = List.of(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);
        List<GovernanceProposalStatus> statusesFilter =  new ArrayList<>();

        Pageable page = PageRequest.of(0, 10);
        Page<GovernanceProposal> found = governanceProposalRepository.findAll(GovernanceProposalSpecs.actionTypeIn(actionsFilter), page);

        // then
        assertEquals(2, found.getContent().size());
        assertEquals(p1, found.getContent().get(0));
        assertEquals(p2, found.getContent().get(1));
    }

    @Test
    public void whenActionTypeAndStatusFilterProvided_thenReturnMatchingProposals() {

        GovernanceProposal p1 = new GovernanceProposal();
        p1.setId("1");
        p1.setHandle(UUID.randomUUID());
        p1.setProposerId("proposer_123");
        p1.setSubjectId("subject_123");
        p1.setStatus(GovernanceProposalStatus.APPROVED);
        p1.setActionType(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);

        GovernanceProposal p2 = new GovernanceProposal();
        p2.setId("3");
        p2.setHandle(UUID.randomUUID());
        p2.setProposerId("proposer_1");
        p2.setSubjectId("subject_1");
        p2.setStatus(GovernanceProposalStatus.APPROVED);
        p2.setActionType(GovernanceActionType.ONBOARD_STAKEHOLDER);
        p2.setCreated(LocalDateTime.now().minusDays(1));

        // given
        List<GovernanceProposal> proposals = new ArrayList<GovernanceProposal>() {
            {
                add(p1);
                add(p2);
            }
        };

        proposals.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        List<GovernanceActionType> actionsFilter = List.of(GovernanceActionType.NEW_LEGAL_PROSE_TEMPLATE);
        List<GovernanceProposalStatus> statusesFilter = List.of(GovernanceProposalStatus.APPROVED);

        Specification spec = Specification.where(GovernanceProposalSpecs.orderByCreated());

        Pageable page = PageRequest.of(0, 10);
        Page<GovernanceProposal> found = governanceProposalRepository.findAll(spec, page);

        // then
        assertEquals(2, found.getContent().size());
        assertEquals(p2, found.getContent().get(0));
        assertEquals(p1, found.getContent().get(1));
    }
}
