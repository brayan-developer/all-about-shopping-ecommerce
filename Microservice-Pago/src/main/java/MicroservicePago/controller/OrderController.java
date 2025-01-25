package MicroservicePago.controller;


import MicroservicePago.entity.OrderItem;
import MicroservicePago.payload.ApiResponse;
import MicroservicePago.request.OrderRequest;
import MicroservicePago.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static MicroservicePago.constants.GeneralConstants.DEFAULT_PAGE_NUMBER;
import static MicroservicePago.constants.GeneralConstants.ID_IN_PATH;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final IOrderService orderService;

    @GetMapping(ID_IN_PATH)
    public List<OrderItem> getOrderById(@PathVariable Long id, @RequestHeader("${gateway.custom-headers.user-id}")
                                        String userId)  {
        return orderService.getOrderById(id, Long.valueOf(userId)).getOrderItems();
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createOrder(@RequestBody OrderRequest orderRequest,
                                                   @RequestHeader("${gateway.custom-headers.user-id}")Long userId,
                                                   @RequestHeader("Authorization") String token)  {

        return orderService.createOrder(orderRequest, userId, token);
    }

    @GetMapping
    public ResponseEntity<ApiResponse> getOrderByUser(@RequestHeader("${gateway.custom-headers.user-id}")Long userId,
                                                      @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER)
                                                                    int page){
        return new ResponseEntity<>(ApiResponse.success(orderService.getOrdersByUser(userId, page)),
                HttpStatus.OK);
    }


    @PostMapping("/payment-notification")
    public void handlePaymentNotification(@RequestBody String payload) {
        try {
            System.out.println("notification: " + payload);
            orderService.handlePaymentNotification(payload);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }





}
