package com.natslash.inventoryservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.natslash.inventoryservice.model.Inventory;
import com.natslash.inventoryservice.repository.InventoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    @Autowired
    private InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public Boolean isInStock(String skuCode) {
        Boolean isInventoryPresent = !(inventoryRepository.findBySkuCode(skuCode).getSkuCode().isEmpty());
        log.info("Inventory is fetched: ", isInventoryPresent);
        return isInventoryPresent;
    }

    @Transactional(readOnly = true)
    public Inventory getInventoryBySkuCode(String skuCode) {
        Inventory inventory = inventoryRepository.findBySkuCode(skuCode);
        log.info("Inventory {}  fetched: ", inventory.getSkuCode());
        return inventory;
    }
}
