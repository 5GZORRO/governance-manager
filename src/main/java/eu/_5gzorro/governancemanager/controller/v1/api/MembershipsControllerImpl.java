package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.controller.v1.response.PagedMembersResponse;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class MembershipsControllerImpl implements MembershipsController {

    private static final Logger log = LogManager.getLogger(MembershipsControllerImpl.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthData authData;

    @Override
    public ResponseEntity<String> applyForMembership(@Valid NewMembershipRequest request) {

        String proposalIdentifier = memberService.processMembershipApplication(request);
        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }

    @Override
    public ResponseEntity<MembershipStatusDto> checkMembershipStatus(@Valid String stakeholderId) {

        MembershipStatusDto status = memberService.getMemberStatus(stakeholderId);
        return ResponseEntity
                .ok()
                .body(status);
    }

    @Override
    public ResponseEntity<PagedMembersResponse> getMembers(Pageable pageable, String filterText) {

        Page<MemberDto> page = memberService.getMembers(pageable, filterText);
        PagedMembersResponse responseBody = new PagedMembersResponse(page);

        return ResponseEntity
                .ok()
                .body(responseBody);
    }

    @Override
    public ResponseEntity revokeMembership(@Valid String stakeholderId) {

        final String requestingStakeholderId = authData.getUserId();

        Optional<String> proposalIdentifier = memberService.revokeMembership(requestingStakeholderId, stakeholderId);

        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }
}
