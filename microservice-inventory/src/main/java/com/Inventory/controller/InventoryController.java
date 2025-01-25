package com.Inventory.controller;

import com.Inventory.payload.ApiResponse;
import com.Inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/inventories")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<ApiResponse> getInventoriesByVariants(@RequestParam List<Long> variantIds){
        return new ResponseEntity<>(ApiResponse.success(inventoryService.getReducedInventories(variantIds)),
                HttpStatus.OK);
    }

    @PostMapping("/stock/availability")
    public ApiResponse checkStockAvailability(){
        return inventoryService.checkAvailableStock();
    }

}
