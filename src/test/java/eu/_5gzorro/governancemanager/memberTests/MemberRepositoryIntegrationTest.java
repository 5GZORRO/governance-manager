package eu._5gzorro.governancemanager.memberTests;

import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;
import eu._5gzorro.governancemanager.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MemberRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void whenFindByLegalName_thenReturnMatchingMembers() {
        Member bt = new Member("1", "BT");
        bt.setStatus(MembershipStatus.PENDING);
        bt.setHandle(UUID.randomUUID());

        Member telefonica = new Member("2", "Telefonica");
        telefonica.setStatus(MembershipStatus.PENDING);
        telefonica.setHandle(UUID.randomUUID());

        Member orange = new Member("3", "Orange");
        orange.setStatus(MembershipStatus.PENDING);
        orange.setHandle(UUID.randomUUID());

        // given
        List<Member> members = new ArrayList<Member>() {
            {
                add(bt);
                add(telefonica);
                add(orange);
            }
        };

        members.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        String nameFilter = "Tele";
        Pageable page = PageRequest.of(0, 10);
        Page<Member> found = memberRepository.findByLegalNameContainingIgnoreCase(nameFilter, page);

        // then
        assertEquals(1, found.getTotalElements());
        assertEquals(telefonica, found.getContent().get(0));
    }

    @Test
    public void whenFindByLegalNameDoesntMatchAny_thenReturnNoMembers() {
        Member bt = new Member("1", "BT");
        bt.setHandle(UUID.randomUUID());
        bt.setStatus(MembershipStatus.PENDING);

        Member telefonica = new Member("2", "Telefonica");
        telefonica.setHandle(UUID.randomUUID());
        telefonica.setStatus(MembershipStatus.PENDING);

        Member orange = new Member("3", "Orange");
        orange.setHandle(UUID.randomUUID());
        orange.setStatus(MembershipStatus.PENDING);

        // given
        List<Member> members = new ArrayList<Member>() {
            {
                add(bt);
                add(telefonica);
                add(orange);
            }
        };

        members.forEach(m -> entityManager.persist(m));
        entityManager.flush();

        // when
        String nameFilter = "Bart";
        Pageable page = PageRequest.of(0, 10);
        Page<Member> found = memberRepository.findByLegalNameContainingIgnoreCase(nameFilter, page);

        // then
        assertEquals(0, found.getTotalElements());
    }
}
