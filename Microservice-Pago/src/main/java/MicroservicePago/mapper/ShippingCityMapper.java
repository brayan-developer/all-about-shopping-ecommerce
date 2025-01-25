package MicroservicePago.mapper;


import MicroservicePago.dto.ShippingCityDTO;
import MicroservicePago.entity.ShippingCity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ShippingDistrictMapper.class)
public interface ShippingCityMapper {

    ShippingCityDTO toDto(ShippingCity shippingCity);
}
