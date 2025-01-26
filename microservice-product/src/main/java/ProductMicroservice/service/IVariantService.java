package ProductMicroservice.service;

import ProductMicroservice.dto.VariantDetailDTO;
import ProductMicroservice.dto.VariantDTO;
import ProductMicroservice.model.ProductVariant;

import java.util.List;

public interface IVariantService {

    ProductVariant getById(Long id);

    List<VariantDetailDTO> getByIdIn(List<Long> ids);

    List<VariantDTO> getByProductId(Long productId);

}
