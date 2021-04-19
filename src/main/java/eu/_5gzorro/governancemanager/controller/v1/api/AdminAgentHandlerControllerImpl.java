package eu._5gzorro.governancemanager.controller.v1.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.model.AuthData;
import eu._5gzorro.governancemanager.service.DeferredExecutionQueue;
import eu._5gzorro.governancemanager.service.GovernanceProposalService;
import eu._5gzorro.governancemanager.service.MemberService;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
public class AdminAgentHandlerControllerImpl implements AdminAgentHandlerController {

    private static final Logger log = LogManager.getLogger(AdminAgentHandlerControllerImpl.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private GovernanceProposalService governanceProposalService;

    @Autowired
    private AuthData authData;

    @Autowired
    ObjectMapper jsonMapper;

    @Autowired
    DeferredExecutionQueue deferredExecutionQueue;

    @Override
    public ResponseEntity<Void> registerStakeholder(@Valid RegisterStakeholderRequest request) throws JsonProcessingException {

        // Process business logic asynchronously to align with the expectation if ID&P
        deferredExecutionQueue.push(() -> {
            try {
                UUID proposalId = memberService.processMembershipApplication(request);
                log.info(String.format("Proposal created with id: %s.  Request: %s", proposalId, jsonMapper.writeValueAsString(request)));
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        });

        return ResponseEntity
                .accepted()
                .build();
    }

    @Override
    public ResponseEntity<Void> issue(@Valid IssueCredentialRequest request) throws JsonProcessingException {

        deferredExecutionQueue.push(() -> {
            try {
                UUID proposalId = governanceProposalService.processIssueCredentialRequest(request);
                log.info(String.format("Proposal created with id: %s.  Request: %s", proposalId, jsonMapper.writeValueAsString(request)));
            } catch (JsonProcessingException e) {
                log.error(e);
            }
        });

        return ResponseEntity
                .accepted()
                .build();
    }

    @Override
    public ResponseEntity<Void> revoke(@Valid String credentialId) {
        throw new NotImplementedException();
    }
}
