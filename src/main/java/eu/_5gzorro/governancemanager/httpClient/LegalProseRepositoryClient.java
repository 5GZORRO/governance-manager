package eu._5gzorro.governancemanager.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="legalProseRepositoryClient", url = "${integrations.legal-prose-repository.apiBaseUrl}")
public interface LegalProseRepositoryClient {

    @RequestMapping(method = RequestMethod.PUT, value = "/legal-prose-templates/{did}/approve/{accept}")
    void setTemplateApprovalStatus(@PathVariable("did") String did, @PathVariable("accept") boolean accept);
}
