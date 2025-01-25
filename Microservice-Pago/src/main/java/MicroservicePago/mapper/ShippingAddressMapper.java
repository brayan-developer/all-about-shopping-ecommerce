package MicroservicePago.mapper;

import MicroservicePago.dto.ShippingAddressDTO;
import MicroservicePago.entity.ShippingAddress;
import MicroservicePago.request.ShippingAddressRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingAddressMapper {
    @Mapping(source = "shippingDistrict", target = "shippingDistrict")
    ShippingAddressDTO toDto(ShippingAddress shippingAddress);

    ShippingAddress toEntity(ShippingAddressRequest shippingAddressRequest);
}
