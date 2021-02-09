package eu._5gzorro.governancemanager.controller.v1.request.governanceActions;

import eu._5gzorro.governancemanager.dto.ActionParamsDto;
import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

public class ProposeGovernanceDecisionRequest {

  @NotNull
  @Schema(allowableValues = {"NEW_LEGAL_PROSE_TEMPLATE","ARCHIVE_LEGAL_PROSE_TEMPLATE","SLA_DISPUTE"}, type = "String")
  private GovernanceActionType actionType;

    @NotNull
    @Valid
    private ActionParamsDto actionParams;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProposeGovernanceDecisionRequest that = (ProposeGovernanceDecisionRequest) o;
        return actionType == that.actionType && actionParams.equals(that.actionParams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(actionType, actionParams);
    }

    @Override
    public String toString() {
        return "ProposeGovernanceDecisionRequest{" +
                "actionType=" + actionType +
                ", actionParams=" + actionParams +
                '}';
    }
}
