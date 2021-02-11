package eu._5gzorro.governancemanager.service;

import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.entity.Member;
import eu._5gzorro.governancemanager.model.exception.MemberStatusException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberService {

    /**
     * Process a membership application request. Creates a member record and generates a governance
     * proposal that is subsequently voted on.
     * @param request
     * @return The DID of the resulting proposal
     */
    String processMembershipApplication(NewMembershipRequest request);

    Page<MemberDto> getMembers(Pageable pageable, String filterText);
    MembershipStatusDto getMemberStatus(String id);
    void processMembershipRevocationRequest(String requestingStakeholderId, String subjectId);
    void revokeMembership(String subjectId);
    void revokeMembership(Member member);
}
