package ProductMicroservice.service.impl;

import ProductMicroservice.dto.*;
import ProductMicroservice.exception.ResourceNotFoundException;
import ProductMicroservice.mapper.ProductMapper;
import ProductMicroservice.model.Product;
import ProductMicroservice.dto.PaginatedResponseDTO;
import ProductMicroservice.repository.ProductRepository;
import ProductMicroservice.service.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static ProductMicroservice.constants.CacheConstants.*;
import static ProductMicroservice.constants.GeneralConstants.*;


@RequiredArgsConstructor
@Service
public class ProductServiceImp implements IProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    @Cacheable(GET_PRODUCT_BY_ID)
    public ProductDetailDto getProductById(Long id) {
        return productRepository.findById(id)
                .map(productMapper::toDetailDto).
                orElseThrow(() -> new ResourceNotFoundException(Product.class.getSimpleName(), FIELD_ID, id));
    }


    @Override
    @Cacheable(GET_PRODUCTS_BY_CATEGORY_BRAND_PRICE)
    public PaginatedResponseDTO<ProductPreviewDTO> getByCategoryAndBrandAndPriceRange(Long categoryId, Long brandId,
                                                                                      BigDecimal minPrice,
                                                                                      BigDecimal maxPrice, int page) {

        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<ProductPreviewDTO> productsPage = productRepository.findByCategoryBrandAndPriceRange(categoryId, brandId,
                minPrice, maxPrice, pageable);

        return new PaginatedResponseDTO<>(productsPage);
    }

    @Override
    @Cacheable(GET_PRICE_RANGE_BY_CATEGORY_BRAND)
    public PriceRangeDTO getPriceRangeByCategoryAndBrand(Long categoryId, Long brandId) {
        return productRepository.findPriceRangeByCategoryAndBrand(categoryId, brandId);
    }

    @Override
    @Cacheable(GET_RANDOM_PRODUCTS_BY_CATEGORY)
    public List<ProductPreviewDTO> getRandomProductsByCategory(Long categoryId) {

        List<Long> randomIds =  productRepository.findRandomProductIdsByCategory(categoryId, DEFAULT_PRODUCT_LIMIT);

        return getProductsByIds(randomIds);
    }


    @Override
    @Cacheable(GET_PRODUCTS_BY_SEARCH_TERM)
    public PaginatedResponseDTO<ProductPreviewDTO> getBySearchTerm(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, DEFAULT_PAGE_SIZE);
        Page<ProductPreviewDTO> products = productRepository.findProductsBySearchTerm(searchTerm, pageable);
        return new PaginatedResponseDTO<>(products);
    }

    @Override
    @Cacheable(GET_PRICE_RANGE_BY_SEARCH_TERM)
    public PriceRangeDTO getPriceRangeBySearchTerm(String searchTerm) {
        return productRepository.findPriceRangeBySearchTerm(searchTerm);
    }


    @Override
    @Cacheable(GET_PRODUCTS_BY_IDS)
    public List<ProductPreviewDTO> getProductsByIds(List<Long> ids){
        return productRepository.findProductsByIds(ids);
    }




}