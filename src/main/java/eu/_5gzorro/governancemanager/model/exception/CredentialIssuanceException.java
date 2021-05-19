package eu._5gzorro.governancemanager.model.exception;

public class CredentialIssuanceException extends RuntimeException {

    public CredentialIssuanceException(String message, Throwable cause) {
        super(message, cause);
    }

    public CredentialIssuanceException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return String.format("An error occurred issuing a credential", this.getCause());
    }
}
