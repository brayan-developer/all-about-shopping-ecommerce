package ProductMicroservice.service;

import ProductMicroservice.dto.CartItemOrderDTO;
import ProductMicroservice.request.CartItemRequest;


public interface ICartItemService {


    CartItemOrderDTO saveCartItem(CartItemRequest cartItemRequest, Long userId);

    CartItemOrderDTO updateCartItem(CartItemRequest cartItemRequest, Long userId);

    void deleteCartItem(Long cartItemId, Long userId);
}
