package com.natslash.inventoryservice.repository;

import com.natslash.inventoryservice.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findBySkuCode(String skuCode);
    List<Inventory> findBySkuCodeIn(List<String> skuCode);
}
