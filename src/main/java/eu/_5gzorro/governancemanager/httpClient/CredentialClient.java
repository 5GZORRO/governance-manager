package eu._5gzorro.governancemanager.httpClient;

import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.IssueCredentialRequest;
import eu._5gzorro.governancemanager.controller.v1.request.adminAgentHandler.RegisterStakeholderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="credentialClient", url = "${integrations.identity-permissions.apiBaseUrl}")
public interface CredentialClient {

    @RequestMapping(method = RequestMethod.POST, value = "/issuer/issue_stakeholder/{_id}")
    void issueStakeholderCredential(@PathVariable("_id") String id, @RequestBody RegisterStakeholderRequest request);

    @RequestMapping(method = RequestMethod.POST, value = "/issuer/issuer_requested_credential/{holder_request_id}")
    void issueCredential(@PathVariable("holder_request_id") String holderRequestId, @RequestBody IssueCredentialRequest request);
}
