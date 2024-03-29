package eu._5gzorro.governancemanager.model.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import eu._5gzorro.governancemanager.controller.v1.request.governanceActions.ProposeGovernanceDecisionRequest;
import eu._5gzorro.governancemanager.dto.ActionParamsDto;
import eu._5gzorro.governancemanager.dto.GovernanceProposalDto;
import eu._5gzorro.governancemanager.model.entity.GovernanceProposal;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;

public class GovernanceProposalMapper {

    public static GovernanceProposalDto toGovernanceProposalDto(GovernanceProposal proposal) {
        GovernanceProposalDto dto = new GovernanceProposalDto();
        dto.setProposalId(proposal.didAssigned() ? proposal.getDid() : proposal.getId().toString());
        dto.setActionType(proposal.getActionType());
        dto.setStatus(proposal.getStatus());
        dto.setStatusUpdated(proposal.getUpdated() != null ? proposal.getUpdated() : proposal.getCreated());

        ActionParamsDto actionParams = new ActionParamsDto();
        actionParams.setEntityIdentityId(proposal.getSubjectId());
        actionParams.setEvidence(proposal.getEvidence());
        dto.setActionParams(actionParams);

        return dto;
    }

    public static GovernanceProposal fromProposeGovernanceDecisionRequest(String requestingStakeholderId, ProposeGovernanceDecisionRequest request) {

        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setProposerId(requestingStakeholderId);
        proposal.setSubjectId(request.getActionParams().getEntityIdentityId());
        proposal.setActionType(request.getActionType());
        proposal.setEvidence(request.getActionParams().getEvidence());

        return proposal;
    }

    public static GovernanceProposal fromIssueCredentialRequest(String requestingStakeholderId, IssueCredentialRequest request) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setProposerId(requestingStakeholderId);
        proposal.setSubjectId(request.getCredentialSubject().getId());
        proposal.setActionType(GovernanceActionType.ISSUE_CREDENTIAL);
        proposal.setIssueCredentialRequest(objectMapper.writeValueAsBytes(request));

        return proposal;
    }

    public static GovernanceProposal fromNewMembershipRequest(String proposingStakeholderId, RegisterStakeholderRequest request) {

        GovernanceProposal proposal = new GovernanceProposal();
        proposal.setProposerId(proposingStakeholderId);
        proposal.setSubjectId(request.getStakeholderClaim().getDid());
        proposal.setActionType(GovernanceActionType.ONBOARD_STAKEHOLDER);

        return proposal;
    }
}
