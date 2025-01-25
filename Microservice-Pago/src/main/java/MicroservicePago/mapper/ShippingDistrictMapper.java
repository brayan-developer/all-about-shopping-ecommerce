package MicroservicePago.mapper;

import MicroservicePago.dto.ShippingDistrictDTO;
import MicroservicePago.entity.ShippingDistrict;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShippingDistrictMapper {
    ShippingDistrictDTO toDto(ShippingDistrict shippingDistrict);
}
