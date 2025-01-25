package ProductMicroservice.controller;

import ProductMicroservice.payload.ApiResponse;
import ProductMicroservice.request.CartItemRequest;
import ProductMicroservice.service.ICartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ProductMicroservice.constants.GeneralConstants.ID_IN_PATH;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/carts/items")
public class CartItemController {


    private final ICartItemService cartItemService;

    @PostMapping
    public ResponseEntity<ApiResponse> saveCartItem(@Valid @RequestBody CartItemRequest cartItemsRequest,
                                                    @RequestHeader("${gateway.custom-headers.user-id}") Long userId){
        return new ResponseEntity<>(ApiResponse.success(cartItemService.saveCartItem(cartItemsRequest,
                                                        userId)), HttpStatus.OK);
    }

    @PutMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse>updateCartItem(@PathVariable Long id ,
                                                      @Valid @RequestBody CartItemRequest cartItemsRequest,
                                                      @RequestHeader("${gateway.custom-headers.user-id}")Long userId){
        cartItemsRequest.setId(id);
        return new ResponseEntity<>(ApiResponse.success(cartItemService.updateCartItem(cartItemsRequest,
                                                        userId)), HttpStatus.OK);
    }

    @DeleteMapping(ID_IN_PATH)
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long id,
                                                      @RequestHeader("${gateway.custom-headers.user-id}") Long userId){
        cartItemService.deleteCartItem(id, userId);
        return new ResponseEntity<>(ApiResponse.success(), HttpStatus.NO_CONTENT);
    }
}
