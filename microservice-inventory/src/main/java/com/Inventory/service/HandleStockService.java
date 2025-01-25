package com.Inventory.service;

import com.Inventory.client.CartClient;
import com.Inventory.dto.CartDTO;
import com.Inventory.model.ProductInventory;
import com.Inventory.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class HandleStockService {

    private final InventoryRepository inventoryRepository;
    private final CartClient cartClient;
    public void reduceStock(Map<Long, Integer> variants) {
        List<ProductInventory> inventories = inventoryRepository.findByVariantIdIn(variants.keySet().stream().toList());


        inventories.forEach(inventory -> {
            int currentStock = inventory.getStock();
            int reduceAmount = variants.getOrDefault(inventory.getVariantId(), 0);

            if (currentStock >= reduceAmount) {
                inventory.setStock(currentStock - reduceAmount);
            } else {
                throw new RuntimeException("Some products do not have enough stock");

            }
        });

        inventoryRepository.saveAll(inventories);
    }

    public CartDTO getCartByUser(){
        return cartClient.getCartByUser();
    }


}
