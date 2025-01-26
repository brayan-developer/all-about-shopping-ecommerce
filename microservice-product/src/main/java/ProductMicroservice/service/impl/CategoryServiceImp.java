package ProductMicroservice.service.impl;

import ProductMicroservice.dto.BrandDTO;
import ProductMicroservice.mapper.BrandMapper;
import ProductMicroservice.model.Category;
import ProductMicroservice.repository.CategoryRepository;
import ProductMicroservice.repository.ProductRepository;
import ProductMicroservice.service.ICategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static ProductMicroservice.constants.CacheConstants.GET_ALL_CATEGORIES;
import static ProductMicroservice.constants.CacheConstants.GET_BRANDS_BY_CATEGORY;

@RequiredArgsConstructor
@Service
public class CategoryServiceImp implements ICategoryService {


    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    private final BrandMapper brandMapper;


    @Override
    @Cacheable(GET_ALL_CATEGORIES)
    public List<Category> getAllCategories() {
        return (List<Category>) categoryRepository.findAll();
    }

    @Override
    @Cacheable(GET_BRANDS_BY_CATEGORY)
    public List<BrandDTO> getBrandsByCategory(Long categoryId) {
        return productRepository.findBrandsByCategoryId(categoryId).stream()
                .map(brandMapper::toBrandDto).collect(Collectors.toList());
    }

}
