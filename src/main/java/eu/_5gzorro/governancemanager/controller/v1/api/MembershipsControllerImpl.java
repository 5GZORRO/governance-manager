package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.membership.NewMembershipRequest;
import eu._5gzorro.governancemanager.dto.MemberDto;
import eu._5gzorro.governancemanager.dto.MembershipStatusDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
public class MembershipsControllerImpl implements MembershipsController {

    private static final Logger log = LogManager.getLogger(MembershipsControllerImpl.class);

    @Override
    public ResponseEntity<String> applyForMembership(@Valid NewMembershipRequest request) {
        return ResponseEntity
                .ok()
                .body("DID");
    }

    @Override
    public ResponseEntity<MembershipStatusDto> checkMembershipStatus(@Valid String stakeholderId) {
        return ResponseEntity
                .ok()
                .body(null);
    }

    @Override
    public ResponseEntity<Page<MemberDto>> getMembers(Pageable pageable, Optional<String> filterText) {
        return ResponseEntity
                .ok()
                .body(null);
    }

    @Override
    public ResponseEntity revokeMembership(@Valid String stakeholderId) {
        return ResponseEntity
                .ok()
                .build();
    }
}
