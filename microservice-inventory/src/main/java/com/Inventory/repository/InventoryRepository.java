package com.Inventory.repository;

import com.Inventory.model.ProductInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InventoryRepository extends JpaRepository<ProductInventory, Long> {


    List<ProductInventory> findByVariantIdIn(List<Long> ids);
}
