package ProductMicroservice.service.impl;

import ProductMicroservice.dto.CartItemOrderDTO;
import ProductMicroservice.exception.ResourceNotFoundException;
import ProductMicroservice.exception.UnauthorizedAccessException;
import ProductMicroservice.mapper.CartItemMapper;
import ProductMicroservice.model.Cart;
import ProductMicroservice.model.CartItem;
import ProductMicroservice.repository.CartItemRepository;
import ProductMicroservice.request.CartItemRequest;
import ProductMicroservice.service.ICartItemService;
import ProductMicroservice.service.ICartService;
import ProductMicroservice.service.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static ProductMicroservice.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static ProductMicroservice.constants.GeneralConstants.FIELD_ID;

@RequiredArgsConstructor
@Service
public class CartItemServiceImpl implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;
    private final IVariantService variantService;
    private final ICartService cartService;


    @Transactional
    @Override
    public CartItemOrderDTO saveCartItem(CartItemRequest cartItemRequest, Long userId) {

        Cart cart = cartService.getOrCreateCartByUser(userId);

        variantService.getById(cartItemRequest.getVariantId());

        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductVariantId(cart.getId(),
                cartItemRequest.getVariantId());

        if (existingCartItem != null) {
            existingCartItem.setQuantity(cartItemRequest.getQuantity());
            return cartItemMapper.toDto(cartItemRepository.save(existingCartItem));
        }

        var newCartItem = cartItemMapper.toEntity(cartItemRequest);
        newCartItem.setCart(cart);
        return cartItemMapper.toDto(cartItemRepository.save(newCartItem));
    }


    @Transactional
    @Override
    public CartItemOrderDTO updateCartItem(CartItemRequest cartItemRequest, Long userId) {
        CartItem cartItem = getById(cartItemRequest.getId());

        validateCartItemOwnership(cartItem, userId);
        cartItem.setQuantity(cartItemRequest.getQuantity());
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Transactional
    @Override
    public void deleteCartItem(Long cartItemId, Long userId) {
        CartItem cartItem = getById(cartItemId);
        validateCartItemOwnership(cartItem, userId);
        cartItemRepository.delete(cartItem);
    }

    private CartItem getById(Long id){
        return cartItemRepository.findById(id).orElseThrow( () ->
                new ResourceNotFoundException(CartItem.class.getSimpleName(), FIELD_ID , id));
    }

    private void validateCartItemOwnership(CartItem cartItem, Long userId) {
        if (!cartItem.getCart().getUserId().equals(userId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }
    }



}
