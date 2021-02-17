package eu._5gzorro.governancemanager.model.exception;

import javax.persistence.EntityNotFoundException;

public class MemberNotFoundException extends EntityNotFoundException {

    private final String id;

    public MemberNotFoundException(String id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return String.format("Member with id '%s' not found", id);
    }
}
