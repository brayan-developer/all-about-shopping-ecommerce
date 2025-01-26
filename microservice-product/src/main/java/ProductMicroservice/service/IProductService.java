package ProductMicroservice.service;

import ProductMicroservice.dto.*;
import ProductMicroservice.dto.PaginatedResponseDTO;

import java.math.BigDecimal;
import java.util.List;

public interface IProductService {

    PaginatedResponseDTO<ProductPreviewDTO> getByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId,
                                                                               BigDecimal minPrice, BigDecimal maxPrice,
                                                                               int page);

    PriceRangeDTO getPriceRangeByCategoryAndBrand(Long categoryId, Long brandId);

    List<ProductPreviewDTO> getRandomProductsByCategory(Long categoryId);

    PriceRangeDTO getPriceRangeBySearchTerm(String searchTerm);

    PaginatedResponseDTO<ProductPreviewDTO> getBySearchTerm (String searchTerm, int page);
    ProductDetailDto getProductById(Long id);

    List<ProductPreviewDTO> getProductsByIds(List<Long> ids);


}