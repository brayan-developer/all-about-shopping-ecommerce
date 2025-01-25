package MicroservicePago.service;

import MicroservicePago.dto.ShippingAddressDTO;
import MicroservicePago.request.ShippingAddressRequest;

import java.util.List;

public interface IShippingAddressService {

    List<ShippingAddressDTO> getByUserId(Long userId);
    ShippingAddressDTO saveShippingAddress(ShippingAddressRequest shippingAddressRequest , Long userId);

    ShippingAddressDTO updateShippingAddress(Long id, ShippingAddressRequest shippingAddressRequest, Long userId);
}
