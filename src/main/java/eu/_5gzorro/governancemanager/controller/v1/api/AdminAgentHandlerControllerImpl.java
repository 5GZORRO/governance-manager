package eu._5gzorro.governancemanager.controller.v1.api;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterRequest;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.service.MemberService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
public class AdminAgentHandlerControllerImpl implements AdminAgentHandlerController {

    private static final Logger log = LogManager.getLogger(AdminAgentHandlerControllerImpl.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private AuthData authData;

    @Override
    public ResponseEntity<UUID> registerStakeholder(@Valid RegisterRequest request) {

        UUID proposalIdentifier = memberService.processMembershipApplication(request);
        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }

    @Override
    public ResponseEntity issue(@Valid IssueRequest request) {
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity revoke(@Valid String credentialId) {
        return ResponseEntity.ok().build();
    }
}
