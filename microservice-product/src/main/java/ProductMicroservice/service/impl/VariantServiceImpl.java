package ProductMicroservice.service.impl;

import ProductMicroservice.dto.VariantDetailDTO;
import ProductMicroservice.dto.VariantDTO;
import ProductMicroservice.exception.ResourceNotFoundException;
import ProductMicroservice.mapper.VariantMapper;
import ProductMicroservice.model.ProductVariant;
import ProductMicroservice.repository.ProductVariantRepository;
import ProductMicroservice.service.IVariantService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ProductMicroservice.constants.CacheConstants.*;
import static ProductMicroservice.constants.GeneralConstants.FIELD_ID;

@RequiredArgsConstructor
@Service
public class VariantServiceImpl implements IVariantService {

    private final ProductVariantRepository productVariantRepository;
    private final VariantMapper variantMapper;

    @Override
    @Cacheable(GET_VARIANT_BY_ID)
    public ProductVariant getById(Long variantId) {
        return productVariantRepository.findById(variantId).orElseThrow(() ->
                new ResourceNotFoundException(ProductVariant.class.getSimpleName(), FIELD_ID, variantId));
    }

    @Override
    @Cacheable(GET_VARIANTS_BY_IDS)
    public List<VariantDetailDTO> getByIdIn(List<Long> ids) {
        return productVariantRepository.findByIdIn(ids).stream()
                .map(variantMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Cacheable(GET_VARIANTS_BY_PRODUCT)
    public List<VariantDTO> getByProductId(Long productId) {
        return productVariantRepository.findByProductId(productId).stream()
                .map(variantMapper::toDetailDto).collect(Collectors.toList());
    }

}
