package ProductMicroservice.dto;

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
public class VariantDetailDTO {

    private Long id;

    private Long productId;

    private String name;

    private BigDecimal price;

    private String imageUrl;

    private List<ProductVariantAttribute> variantAttributes;
}
