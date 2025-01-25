package MicroservicePago.service;

import MicroservicePago.client.InventoryClient;
import MicroservicePago.dto.CartItemDTO;
import MicroservicePago.payload.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderValidatorService {

    private final InventoryClient inventoryClient;

    public ApiResponse validateStock(Long userId) {
        ApiResponse apiResponse = inventoryClient.checkAvailableStock();
        Object object = apiResponse.getData();

        if (!apiResponse.isSuccess()) {
            return ApiResponse.error(apiResponse.getMessage(), object);
        }
        List<CartItemDTO> cartItems = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        cartItems = objectMapper.convertValue(object, new TypeReference<>() {});



        return ApiResponse.success(cartItems);
    }



}
