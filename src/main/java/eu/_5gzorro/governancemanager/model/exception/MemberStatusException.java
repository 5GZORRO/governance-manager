package eu._5gzorro.governancemanager.model.exception;

import eu._5gzorro.governancemanager.model.enumeration.MembershipStatus;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class MemberStatusException extends RuntimeException {

    private final Set<MembershipStatus> permittedStatuses;
    private final MembershipStatus actualStatus;

    public MemberStatusException(Collection<MembershipStatus> permittedStatuses, MembershipStatus actualStatus) {
        this.permittedStatuses = new HashSet<>(permittedStatuses);
        this.actualStatus = actualStatus;
    }

    public MemberStatusException(MembershipStatus permittedStatus, MembershipStatus actualStatus) {
        this(new HashSet<>(Collections.singleton(permittedStatus)), actualStatus);
    }

    @Override
    public String getMessage() {
        return String.format("This operation is not permitted on a Member in %s state. Permitted states: %s", actualStatus, permittedStatuses);
    }
}
