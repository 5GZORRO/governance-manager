package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.dto.identityPermissions.DIDStateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface MemberService {

    /**
     * Process a membership application request. Creates a member record and generates a governance
     * proposal that is subsequently voted on.
     * @param request
     * @return The UUID Handle of the resulting proposal
     */
    UUID processMembershipApplication(RegisterStakeholderRequest request) throws JsonProcessingException;

    Page<MemberDto> getMembers(Pageable pageable, String filterText);
    MembershipStatusDto getMemberStatus(String id);
    Optional<UUID> revokeMembership(String requestingStakeholderId, String subjectId);
}
