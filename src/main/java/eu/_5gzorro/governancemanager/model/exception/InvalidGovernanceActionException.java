package eu._5gzorro.governancemanager.model.exception;

import eu._5gzorro.governancemanager.model.enumeration.GovernanceActionType;

public class InvalidGovernanceActionException extends RuntimeException {

    private final GovernanceActionType actionType;

    public InvalidGovernanceActionException(GovernanceActionType actionType) {
        this.actionType = actionType;
    }

    @Override
    public String getMessage() {

        if(actionType == GovernanceActionType.REVOKE_STAKEHOLDER_MEMBERSHIP || actionType == GovernanceActionType.ONBOARD_STAKEHOLDER) {
            return "Invalid Governance Action.  Membership actions should be submitted to members API endpoints";
        }
        else {
            return String.format("Invalid Governance Action Proposed (%s)", actionType);
        }
    }
}
