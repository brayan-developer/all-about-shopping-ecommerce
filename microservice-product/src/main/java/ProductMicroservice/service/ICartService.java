package ProductMicroservice.service;

import ProductMicroservice.dto.CartOrderDTO;
import ProductMicroservice.dto.CartUserDTO;
import ProductMicroservice.model.Cart;

public interface ICartService {

    CartOrderDTO getCartToCreateOrder(Long userId);

    CartUserDTO getCartForUser(Long userId);

    Cart getOrCreateCartByUser(Long userId);

}
