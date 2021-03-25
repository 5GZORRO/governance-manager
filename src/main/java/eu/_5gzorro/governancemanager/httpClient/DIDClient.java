package eu._5gzorro.governancemanager.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value="didClient", url = "${integrations.identity-permissions.apiBaseUrl}")
public interface DIDClient {

    @RequestMapping(method = RequestMethod.GET, value = "/holder/create_did")
    void create(@RequestParam(value="handler_url") String handlerUrl, @RequestParam(value="token") String authToken);
}
