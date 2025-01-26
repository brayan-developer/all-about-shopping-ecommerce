package ProductMicroservice.service;

import ProductMicroservice.dto.BrandDTO;
import ProductMicroservice.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();
    List<BrandDTO> getBrandsByCategory(Long categoryId);
}
