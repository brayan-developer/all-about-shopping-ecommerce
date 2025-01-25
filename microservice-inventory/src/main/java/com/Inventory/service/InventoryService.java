package com.Inventory.service;

import com.Inventory.dto.ProductInventoryDTO;
import com.Inventory.payload.ApiResponse;

import java.util.List;

public interface InventoryService {

    List<ProductInventoryDTO> getReducedInventories(List<Long> ids);


    ApiResponse checkAvailableStock();
}
