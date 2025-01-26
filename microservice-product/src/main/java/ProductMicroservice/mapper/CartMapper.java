package ProductMicroservice.mapper;


import ProductMicroservice.dto.CartOrderDTO;
import ProductMicroservice.dto.CartUserDTO;
import ProductMicroservice.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring" , uses = CartItemMapper.class)
public interface CartMapper {

    @Mapping(source = "cartItems", target = "cartItems")
    CartOrderDTO toDto(Cart cart);

    CartUserDTO toDTOUser(Cart cart);
}
