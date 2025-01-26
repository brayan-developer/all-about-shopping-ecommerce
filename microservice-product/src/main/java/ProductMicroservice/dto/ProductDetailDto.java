package ProductMicroservice.dto;

import ProductMicroservice.model.ProductMediaMapping;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ProductDetailDto {

    private Long id;

    private String name;
    private String description;

    private String mainImage;

    private BigDecimal price;

    private Double discount;

    private BrandDTO brand;

    private Double qualification;

    private Date createdAt;

    private List<ProductMediaMapping> productMediaMappings;
}
