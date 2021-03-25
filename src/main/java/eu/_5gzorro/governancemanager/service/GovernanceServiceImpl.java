package eu._5gzorro.governancemanager.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GovernanceServiceImpl implements GovernanceService {

    private static final Logger log = LogManager.getLogger(GovernanceServiceImpl.class);

    @Autowired
    private IdentityAndPermissionsApiClient identityClientService;


    @Override
    public boolean canIssueCredential(GovernanceProposal proposal) {
        return true;
    }

    @Override
    public void issueCredential(GovernanceProposal proposal) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

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
}
