package MicroservicePago.service;

import MicroservicePago.dto.OrderDTO;
import MicroservicePago.dto.PaginatedResponseDTO;
import MicroservicePago.payload.ApiResponse;
import MicroservicePago.request.OrderRequest;
import org.springframework.http.ResponseEntity;

public interface IOrderService {

    OrderDTO getOrderById(Long id, Long userId);
    PaginatedResponseDTO<OrderDTO> getOrdersByUser(Long userId, int page);

    ResponseEntity<ApiResponse> createOrder(OrderRequest orderRequest, Long userId, String token);


    void handlePaymentNotification(String payload);
}
