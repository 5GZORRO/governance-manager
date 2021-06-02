package eu._5gzorro.governancemanager.httpClient;

import eu._5gzorro.governancemanager.dto.identityPermissions.StakeholderStatusDto;
import eu._5gzorro.governancemanager.httpClient.requests.CreateDidRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value="didClient", url = "${integrations.identity-permissions.myAgentBaseUrl}")
public interface DIDClient {

    @RequestMapping(method = RequestMethod.POST, value = "/holder/create_did")
    void create(@RequestBody CreateDidRequest request);

    @RequestMapping(method = RequestMethod.GET, value = "/holder/read_stakeholder_status")
    StakeholderStatusDto getMyStakeholderCredential();
}
