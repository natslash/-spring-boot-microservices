package com.natslash.inventoryservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.natslash.inventoryservice.model.Inventory;
import com.natslash.inventoryservice.service.InventoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/ispresent/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public Boolean isInStock(@PathVariable("sku-code") String skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventory(@PathVariable("sku-code") String skuCode) {
        return inventoryService.getInventoryBySkuCode(skuCode);
    }

}
