package com.natslash.inventoryservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.natslash.inventoryservice.model.Inventory;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Inventory findBySkuCode(String skuCode);
}
