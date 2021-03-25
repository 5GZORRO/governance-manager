package eu._5gzorro.governancemanager.controller.v1.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.service.GovernanceProposalService;
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
    private GovernanceProposalService governanceProposalService;

    @Autowired
    private AuthData authData;

    @Override
    public ResponseEntity<UUID> registerStakeholder(@Valid RegisterStakeholderRequest request) throws JsonProcessingException {

        UUID proposalIdentifier = memberService.processMembershipApplication(request);

        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }

    @Override
    public ResponseEntity issue(@Valid IssueCredentialRequest request) throws JsonProcessingException {
        UUID proposalIdentifier = governanceProposalService.processIssueCredentialRequest(request);
        return ResponseEntity
                .ok()
                .body(proposalIdentifier);
    }

    @Override
    public ResponseEntity revoke(@Valid String credentialId) {
        return ResponseEntity.ok().build();
    }
}
