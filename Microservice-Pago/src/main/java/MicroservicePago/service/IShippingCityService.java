package MicroservicePago.service;

import MicroservicePago.dto.ShippingCityDTO;
import MicroservicePago.entity.ShippingCity;

import java.util.List;

public interface IShippingCityService {

    List<ShippingCityDTO> getActiveCitiesAndDistricts();
}
