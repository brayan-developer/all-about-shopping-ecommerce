package com.Inventory.client;


import com.Inventory.dto.OrderItemDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import static com.Inventory.constants.GeneralConstants.ID_IN_PATH;

@FeignClient(
        name = "msvc-order",
        url = "${gateway.url}/api/orders"
)
public interface OrderClient {

    @GetMapping(ID_IN_PATH)
    List<OrderItemDTO> getOrderById(@PathVariable Long id, @RequestHeader("Authorization") String token);
}
