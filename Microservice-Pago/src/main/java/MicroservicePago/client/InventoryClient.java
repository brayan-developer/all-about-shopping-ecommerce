package MicroservicePago.client;

import MicroservicePago.config.FeignClientConfig;
import MicroservicePago.payload.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "msvc-inventory", url = "${gateway.url}/api/inventories" , configuration = FeignClientConfig.class)
public interface InventoryClient {


    @PostMapping("/stock/availability")
    ApiResponse checkAvailableStock();
}
