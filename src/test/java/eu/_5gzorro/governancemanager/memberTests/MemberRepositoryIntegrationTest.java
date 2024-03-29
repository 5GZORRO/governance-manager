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

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MemberRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void whenFindByLegalName_thenReturnMatchingMembers() {

        Member bt = new Member("1", "BT", "O=OperatorA,OU=Manchester,L=Manchester,C=UK");
        bt.setStatus(MembershipStatus.PENDING);

        Member telefonica = new Member("2", "Telefonica", "O=OperatorB,OU=Zurich,L=Zurich,C=CH");
        telefonica.setStatus(MembershipStatus.PENDING);

        Member orange = new Member("3", "Orange", "O=OperatorC,OU=Paris,L=Paris,C=FR");
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
        String nameFilter = "Tele";
        Pageable page = PageRequest.of(0, 10);
        Page<Member> found = memberRepository.findByLegalNameContainingIgnoreCase(nameFilter, page);

        // then
        assertEquals(1, found.getTotalElements());
        assertEquals(telefonica, found.getContent().get(0));
    }

    @Test
    public void whenFindByLegalNameDoesntMatchAny_thenReturnNoMembers() {

        Member bt = new Member("1", "BT", "O=OperatorA,OU=Manchester,L=Manchester,C=UK");
        bt.setStatus(MembershipStatus.PENDING);

        Member telefonica = new Member("2", "Telefonica", "O=OperatorB,OU=Zurich,L=Zurich,C=CH");
        telefonica.setStatus(MembershipStatus.PENDING);

        Member orange = new Member("3", "Orange", "O=OperatorC,OU=Paris,L=Paris,C=FR");
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
