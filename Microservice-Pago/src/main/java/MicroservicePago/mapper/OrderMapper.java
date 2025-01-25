package MicroservicePago.mapper;

import MicroservicePago.dto.OrderDTO;
import MicroservicePago.entity.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDTO toDto(Order order);
}
