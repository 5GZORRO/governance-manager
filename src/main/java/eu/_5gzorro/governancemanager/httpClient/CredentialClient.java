package eu._5gzorro.governancemanager.httpClient;

import eu._5gzorro.governancemanager.httpClient.request.IssueCredentialRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="credentialClient", url = "${integrations.identity-permissions.apiBaseUrl}")
public interface CredentialClient {

    @RequestMapping(method = RequestMethod.POST, value = "/issuer/issuer_requested_credential")
    void issue(IssueCredentialRequest request);
}
