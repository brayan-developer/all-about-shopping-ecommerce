package ProductMicroservice.dto;

import ProductMicroservice.model.ProductMediaMapping;
import ProductMicroservice.model.ProductVariantAttribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VariantDTO {

    private Long id;

    private BigDecimal price;

    private String sku;

    private List<ProductMediaMapping> productMediaMappings;

    private List<ProductVariantAttribute> variantAttributes;
}
