package ProductMicroservice.controller;

import ProductMicroservice.dto.CartOrderDTO;
import ProductMicroservice.payload.ApiResponse;
import ProductMicroservice.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final ICartService cartService;


    @GetMapping
    public ResponseEntity<ApiResponse> getCartByUser(@RequestHeader("${gateway.custom-headers.user-id}")Long userId){
        return new ResponseEntity<>(ApiResponse.success(cartService.getCartForUser(userId)),HttpStatus.OK);
    }

    @GetMapping("/checkout")
    public CartOrderDTO getCartToCreateOrder(@RequestHeader("${gateway.custom-headers.user-id}") Long userId){
        return cartService.getCartToCreateOrder(userId);
    }

}
