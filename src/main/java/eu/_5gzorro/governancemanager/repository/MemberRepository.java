package eu._5gzorro.governancemanager.repository;

import eu._5gzorro.governancemanager.model.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;
import java.util.UUID;

public interface MemberRepository extends PagingAndSortingRepository<Member, String> {
    Page<Member> findByLegalNameContainingIgnoreCase(String legalName, Pageable page);
    Optional<Member> findByHandle(UUID handle);
}
