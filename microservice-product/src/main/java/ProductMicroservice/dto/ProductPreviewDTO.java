package ProductMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductPreviewDTO {

    private Long id;

    private String name;

    private BigDecimal price;

    private String mainImage;

    private Double discount;

    private Double qualification;

    private LocalDateTime createdAt;
}
