package ProductMicroservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CartUserDTO {

    private Long id;

    private Long userId;

    private List<CartItemDTO> cartItems;

}
