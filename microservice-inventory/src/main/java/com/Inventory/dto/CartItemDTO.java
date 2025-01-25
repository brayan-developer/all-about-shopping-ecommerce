package com.Inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class CartItemDTO {

    private Long id;

    private Long variantId;
    private BigDecimal price;

    private int quantity;

}
