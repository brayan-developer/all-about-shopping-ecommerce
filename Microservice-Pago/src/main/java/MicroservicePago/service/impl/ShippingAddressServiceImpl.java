package MicroservicePago.service.impl;

import MicroservicePago.dto.ShippingAddressDTO;
import MicroservicePago.entity.ShippingAddress;
import MicroservicePago.entity.ShippingDistrict;
import MicroservicePago.exception.ResourceNotFoundException;
import MicroservicePago.exception.UnauthorizedAccessException;
import MicroservicePago.mapper.ShippingAddressMapper;
import MicroservicePago.repository.ShippingAddressRepository;
import MicroservicePago.repository.ShippingDistrictRepository;
import MicroservicePago.request.ShippingAddressRequest;
import MicroservicePago.service.IShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import static MicroservicePago.constants.CacheConstants.GET_SHIPPING_DISTRICT_BY_ID;
import static MicroservicePago.constants.ErrorMessageConstants.UNAUTHORIZED_ACCESS;
import static MicroservicePago.constants.GeneralConstants.FIELD_ID;

@RequiredArgsConstructor
@Service
public class ShippingAddressServiceImpl implements IShippingAddressService {

    private final ShippingAddressRepository shippingAddressRepository;
    private final ShippingAddressMapper shippingAddressMapper;
    private final ShippingDistrictRepository shippingDistrictRepository;

    @Override
    public List<ShippingAddressDTO> getByUserId(Long userId) {
        return shippingAddressRepository.findByUserId(userId).stream()
                .map(shippingAddressMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ShippingAddressDTO saveShippingAddress(ShippingAddressRequest shippingAddressRequest, Long userId) {
        ShippingDistrict shippingDistrict = getShippingDistrictById(shippingAddressRequest.getShipping_district_id());

        ShippingAddress shippingAddress = shippingAddressMapper.toEntity(shippingAddressRequest);
        shippingAddress.setUserId(userId);
        shippingAddress.setShippingDistrict(shippingDistrict);
        return shippingAddressMapper.toDto(shippingAddressRepository.save(shippingAddress));
    }



    @Override
    @Transactional
    public ShippingAddressDTO updateShippingAddress(Long id, ShippingAddressRequest shippingAddressRequest, Long userId) {

        ShippingAddress existingAddress = getShippingAddressById(id);

        // Check userId compatibility
        if (!existingAddress.getUserId().equals(userId)) {
            throw new UnauthorizedAccessException(UNAUTHORIZED_ACCESS);
        }

        ShippingDistrict shippingDistrict = getShippingDistrictById(shippingAddressRequest.getShipping_district_id());

        // Assign missing properties
        ShippingAddress updatedAddress = shippingAddressMapper.toEntity(shippingAddressRequest);
        updatedAddress.setId(id);
        updatedAddress.setShippingDistrict(shippingDistrict);
        updatedAddress.setUserId(userId);

        return shippingAddressMapper.toDto(shippingAddressRepository.save(updatedAddress));
    }

    private ShippingAddress getShippingAddressById(Long id){
       return  shippingAddressRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingAddress.class.getSimpleName(),FIELD_ID, id));
    }

    @Cacheable(GET_SHIPPING_DISTRICT_BY_ID)
    private ShippingDistrict getShippingDistrictById(Long id){
        return shippingDistrictRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ShippingDistrict.class.getSimpleName(),FIELD_ID, id));
    }


}
