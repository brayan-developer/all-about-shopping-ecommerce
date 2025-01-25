package ProductMicroservice.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CartItemDTO {


    private Long id;

    private VariantDetailDTO productVariant;
    private int quantity;

}
