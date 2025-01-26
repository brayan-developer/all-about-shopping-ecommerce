package ProductMicroservice.mapper;

import ProductMicroservice.dto.CartItemDTO;
import ProductMicroservice.dto.CartItemOrderDTO;
import ProductMicroservice.model.CartItem;
import ProductMicroservice.request.CartItemRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = VariantMapper.class)
public interface CartItemMapper {



    @Mapping(target = "variantId", source = "productVariant.id")
    @Mapping(target = "price", source = "productVariant.price")
    CartItemOrderDTO toDto(CartItem cartItem);

    CartItemDTO toDtoUser(CartItem cartItem);



    @Mapping(target = "productVariant.id", source = "variantId")
    CartItem toEntity(CartItemRequest cartItemRequest);

}
