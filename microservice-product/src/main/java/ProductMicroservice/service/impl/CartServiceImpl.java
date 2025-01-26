package ProductMicroservice.service.impl;

import ProductMicroservice.dto.CartOrderDTO;
import ProductMicroservice.dto.CartUserDTO;
import ProductMicroservice.mapper.CartMapper;
import ProductMicroservice.model.Cart;
import ProductMicroservice.repository.CartRepository;
import ProductMicroservice.service.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements ICartService {

    private final CartRepository cartRepository;
    private final CartMapper cartMapper;

    @Override
    public CartOrderDTO getCartToCreateOrder(Long userId) {
        return cartMapper.toDto(cartRepository.findByUserId(userId));
    }

    @Override
    public CartUserDTO getCartForUser(Long userId) {
        return cartMapper.toDTOUser(cartRepository.findByUserId(userId));
    }

    @Override
    public Cart getOrCreateCartByUser(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);
        if(cart == null){
            return cartRepository.save(Cart.builder().userId(userId).build());
        }
        return cart;
    }
}
