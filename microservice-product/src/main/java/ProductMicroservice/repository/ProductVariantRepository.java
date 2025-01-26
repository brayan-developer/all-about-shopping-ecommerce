package ProductMicroservice.repository;

import ProductMicroservice.model.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {


    List<ProductVariant> findByIdIn(List<Long> ids);

    List<ProductVariant> findByProductId(Long productId);


    /*
    @Query("SELECT DISTINCT pv.size FROM ProductVariant pv WHERE pv.productId = :productId AND pv.color.id = :colorId")
    List<Size> findSizesByProductAndColor(@Param("productId") Long productId, @Param("colorId") Long colorId);

    @Query("SELECT new ProductMicroservice.dto.VariantColorDTO( pv.id, pv.productId, pv.price, " +
            "pv.color, pvi.imageUrl) FROM ProductVariant pv LEFT JOIN pv.variantImages pvi " +
            "WHERE pv.productId = :productId AND pvi.isMain = true " +
            "GROUP BY pv.color, pv.productId, pv.id, pvi.imageUrl")
    List<VariantColorDTO> findDistinctByColor(@Param("productId") Long productId);


    @Query("SELECT new ProductMicroservice.dto.VariantSizeDTO(pv.id, pv.productId, pv.price, pv.size) FROM" +
            " ProductVariant pv WHERE pv.productId = :productId AND pv.color.id = :colorId")
    List<VariantSizeDTO> findSizesByProductAndColor(@Param("productId") Long productId, @Param("colorId") Long colorId);

     */

}
