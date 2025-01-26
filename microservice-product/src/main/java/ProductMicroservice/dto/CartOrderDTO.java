package ProductMicroservice.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CartOrderDTO {
    private Long id;

    private Long userId;

    private List<CartItemOrderDTO> cartItems;
}
