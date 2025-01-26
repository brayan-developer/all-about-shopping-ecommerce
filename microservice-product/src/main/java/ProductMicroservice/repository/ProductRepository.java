package ProductMicroservice.repository;

import ProductMicroservice.dto.PriceRangeDTO;
import ProductMicroservice.dto.ProductPreviewDTO;
import ProductMicroservice.dto.ProductSummaryDTO;
import ProductMicroservice.model.Brand;
import ProductMicroservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT new ProductMicroservice.dto.ProductPreviewDTO(p.id, p.name, p.price, p.mainImage, p.discount, p.qualification, p.createdAt) " +
            "FROM Product p JOIN p.categories c " +
            "WHERE c.id = :categoryId " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId) " +
            "AND (:minPrice IS NULL OR p.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR p.price <= :maxPrice)")
    Page<ProductPreviewDTO> findByCategoryBrandAndPriceRange( @Param("categoryId") Long categoryId, @Param("brandId") Long brandId,
            @Param("minPrice") BigDecimal minPrice,@Param("maxPrice") BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT new ProductMicroservice.dto.PriceRangeDTO(MIN(p.price), MAX(p.price)) " +
            "FROM Product p JOIN p.categories c WHERE c.id = :categoryId " +
            "AND (:brandId IS NULL OR p.brand.id = :brandId)")
    PriceRangeDTO findPriceRangeByCategoryAndBrand( @Param("categoryId") Long categoryId, @Param("brandId") Long brandId);


    @Query("SELECT DISTINCT new ProductMicroservice.dto.ProductPreviewDTO(p.id, p.name, p.price, p.mainImage, p.discount," +
            " p.qualification, p.createdAt) " +
            "FROM Product p JOIN p.categories c WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<ProductPreviewDTO> findProductsBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);

    @Query("SELECT new ProductMicroservice.dto.PriceRangeDTO(MIN(p.price), MAX(p.price)) " +
            "FROM Product p JOIN p.categories c WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
            "OR LOWER(c.name) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    PriceRangeDTO findPriceRangeBySearchTerm(@Param("searchTerm") String searchTerm);


    @Query(value = "SELECT p.id FROM products p " +
            "JOIN product_categories pc ON p.id = pc.product_id " +
            "WHERE pc.category_id = :categoryId " +
            "ORDER BY RANDOM() " +
            "LIMIT :limit", nativeQuery = true)
    List<Long> findRandomProductIdsByCategory(@Param("categoryId") Long categoryId, @Param("limit") int limit);

    @Query("SELECT new ProductMicroservice.dto.ProductPreviewDTO(p.id, p.name, p.price, p.mainImage, p.discount, p.qualification, p.createdAt) " +
            "FROM Product p WHERE p.id IN :ids")
    List<ProductPreviewDTO> findProductsByIds(@Param("ids") List<Long> ids);

    @Query("SELECT DISTINCT p.brand FROM Product p JOIN p.categories c WHERE c.id = :categoryId")
    List<Brand> findBrandsByCategoryId(@Param("categoryId") Long categoryId);
}
