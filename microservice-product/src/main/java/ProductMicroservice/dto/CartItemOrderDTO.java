package ProductMicroservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class CartItemOrderDTO {

    private Long id;

    private Long variantId;
    private BigDecimal price;
    private int quantity;


}
