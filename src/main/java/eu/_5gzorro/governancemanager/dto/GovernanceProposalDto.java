package eu._5gzorro.governancemanager.dto;

import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import eu._5gzorro.governancemanager.model.enumeration.ProposalStatus;

import java.time.LocalDateTime;
import java.util.Objects;

public class GovernanceProposalDto {
    private String proposalId;
    private ProposalStatus status;
    private GovernanceActionType actionType;
    private ActionParamsDto actionParams;
    private LocalDateTime statusUpdated;

    public GovernanceProposalDto() {
    }

    public String getProposalId() {
        return proposalId;
    }

    public void setProposalId(String proposalId) {
        this.proposalId = proposalId;
    }

    public ProposalStatus getStatus() {
        return status;
    }

    public void setStatus(ProposalStatus status) {
        this.status = status;
    }

    public GovernanceActionType getActionType() {
        return actionType;
    }

    public void setActionType(GovernanceActionType actionType) {
        this.actionType = actionType;
    }

    public ActionParamsDto getActionParams() {
        return actionParams;
    }

    public void setActionParams(ActionParamsDto actionParams) {
        this.actionParams = actionParams;
    }

    public LocalDateTime getStatusUpdated() {
        return statusUpdated;
    }

    public void setStatusUpdated(LocalDateTime statusUpdated) {
        this.statusUpdated = statusUpdated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GovernanceProposalDto that = (GovernanceProposalDto) o;
        return proposalId.equals(that.proposalId) && status == that.status && actionType == that.actionType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(proposalId, status, actionType);
    }

    @Override
    public String toString() {
        return "GovernanceProposalDto{" +
                "proposalId='" + proposalId + '\'' +
                ", status=" + status +
                ", actionType=" + actionType +
                ", actionParams=" + actionParams +
                ", statusUpdated=" + statusUpdated +
                '}';
    }
}
