package com.natslash.inventoryservice.controller;


import com.natslash.inventoryservice.dto.InventoryResponse;
import com.natslash.inventoryservice.model.Inventory;
import com.natslash.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    // http://localhost:8082/api/inventory?skuCode=iphone-13&skuCode=iphone13-red
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode) {
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/{sku-code}")
    @ResponseStatus(HttpStatus.OK)
    public Inventory getInventory(@PathVariable("sku-code") String skuCode) {
        return inventoryService.getInventoryBySkuCode(skuCode);
    }

    @GetMapping("/getAllInventory")
    @ResponseStatus(HttpStatus.OK)
    public List<Inventory> getStocks() {
        return inventoryService.getAllInventory();
    }


}
