package ProductMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ProductSummaryDTO {

    private Long id;
    private String name;
    private String mainImage;
    private BigDecimal price;
}
