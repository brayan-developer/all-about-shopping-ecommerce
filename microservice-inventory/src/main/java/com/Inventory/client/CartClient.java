package com.Inventory.client;

import com.Inventory.config.FeignClientConfig;
import com.Inventory.dto.CartDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "msvc-cart",
        url = "${gateway.url}/api/carts", configuration = FeignClientConfig.class
)
public interface CartClient {

    @GetMapping("/checkout")
    CartDTO getCartByUser();
}
