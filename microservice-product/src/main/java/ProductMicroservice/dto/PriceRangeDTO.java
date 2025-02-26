package ProductMicroservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PriceRangeDTO {

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
}
