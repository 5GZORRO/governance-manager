package eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler;

import eu._5gzorro.governancemanager.model.enumeration.CredentialRequestType;

import javax.validation.constraints.NotBlank;

public abstract class CredentialRequest {
    @NotBlank
    private CredentialRequestType type;

    public CredentialRequestType getType() { return type; }

    public void setType() { this.type = type; }

    public CredentialRequest() {}
    public CredentialRequest(CredentialRequestType type) { this.type = type; }
}
