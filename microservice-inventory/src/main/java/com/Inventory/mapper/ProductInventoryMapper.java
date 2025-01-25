package com.Inventory.mapper;

import com.Inventory.dto.ProductInventoryDTO;
import com.Inventory.model.ProductInventory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductInventoryMapper {

    ProductInventoryDTO toDto(ProductInventory productInventory);
}
