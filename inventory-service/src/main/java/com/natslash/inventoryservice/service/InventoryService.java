package com.natslash.inventoryservice.service;

import com.natslash.inventoryservice.dto.InventoryResponse;
import com.natslash.inventoryservice.model.Inventory;
import com.natslash.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        log.info("Checking Inventory");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()
                ).toList();
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        log.info("Inventory {}  fetched: ", inventory.getSkuCode());
        return inventory;
    }

    @Transactional(readOnly = true)
    public List<Inventory> getAllInventory() {
        List<Inventory> inventory = inventoryRepository.findAll();
        log.info("{} items fetched from inventory", inventory.size());
        return inventory;
    }
}
