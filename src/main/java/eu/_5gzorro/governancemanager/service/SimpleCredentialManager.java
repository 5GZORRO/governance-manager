package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.CredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;
import eu._5gzorro.governancemanager.model.mapper.GovernanceProposalMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SimpleCredentialManager implements CredentialManager {

    private static final Logger log = LogManager.getLogger(SimpleCredentialManager.class);
    private final List<CredentialRequestType> requestsSubjectToGovernance = List.of(
            CredentialRequestType.STAKEHOLDER,
            CredentialRequestType.LEGAL_PROSE_TEMPLATE,
            CredentialRequestType.REGULATED_PRODUCT_OFFER
    );

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IdentityAndPermissionsApiClient identityClientService;

    @Autowired
    private GovernanceProposalService proposalService;

    @Autowired
    private MemberService memberService;

    @Override
    public Optional<UUID> processCredentialRequest(String requestingStakeholderId, CredentialRequest request) throws JsonProcessingException {

        boolean isSubjectToGovernance = requestsSubjectToGovernance.contains(request.getType());

        // Create member record if a stakeholder registration request
        if(request.getType().equals(CredentialRequestType.STAKEHOLDER)) {
            memberService.processMembershipApplication((RegisterStakeholderRequest) request, isSubjectToGovernance);
        }

        // Create a proposal if the request is subject to governance
        if(isSubjectToGovernance) {
            GovernanceProposal proposal = GovernanceProposalMapper.fromCredentialRequest(requestingStakeholderId, request);
            return Optional.of(proposalService.processGovernanceProposal(proposal));
        }

        // If no governance, then issue the credential
        issueCredential(request);
        return Optional.empty();
    }

    @Override
    public void issueCredential(GovernanceProposal proposal) throws IOException {

        switch(proposal.getActionType()) {
            case ONBOARD_STAKEHOLDER:
                RegisterStakeholderRequest issueStakeholderCredentialRequest = objectMapper.readValue(proposal.getIssueCredentialRequest(), RegisterStakeholderRequest.class);
                identityClientService.issueStakeholderCredential(issueStakeholderCredentialRequest);
                break;
            default:
                IssueCredentialRequest issueCredentialRequest = objectMapper.readValue(proposal.getIssueCredentialRequest(), IssueCredentialRequest.class);
                identityClientService.issueCredential(issueCredentialRequest);
                break;
        }
    }

    private void issueCredential(CredentialRequest credentialRequest) {
        if(credentialRequest.getType().equals(CredentialRequestType.STAKEHOLDER)) {
            identityClientService.issueStakeholderCredential((RegisterStakeholderRequest)credentialRequest);
        }
        else {
            identityClientService.issueCredential((IssueCredentialRequest)credentialRequest);
        }
    }
}
