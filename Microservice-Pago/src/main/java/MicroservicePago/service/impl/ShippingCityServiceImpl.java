package MicroservicePago.service.impl;

import MicroservicePago.dto.ShippingCityDTO;
import MicroservicePago.mapper.ShippingCityMapper;
import MicroservicePago.repository.ShippingCityRepository;
import MicroservicePago.service.IShippingCityService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

import static MicroservicePago.constants.CacheConstants.GET_ALL_SHIPPING_CITIES;

@RequiredArgsConstructor
@Service
public class ShippingCityServiceImpl implements IShippingCityService {

    private final ShippingCityRepository shippingCityRepository;
    private final ShippingCityMapper shippingCityMapper;
    @Override
    @Cacheable(GET_ALL_SHIPPING_CITIES)
    public List<ShippingCityDTO> getActiveCitiesAndDistricts() {
        return shippingCityRepository.findActiveCitiesAndDistricts().stream().map(shippingCityMapper::toDto).toList();
    }
}
