package com.bookshop.inventoryservice.controller;

import com.bookshop.inventoryservice.dto.InventoryRequest;
import com.bookshop.inventoryservice.dto.InventoryResponse;
import com.bookshop.inventoryservice.model.Inventory;
import com.bookshop.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/inventory")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("skuCode") List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void setInventoryItem(@RequestBody InventoryRequest inventoryRequest) {
        inventoryService.setInventoryItem(inventoryRequest);
    }
}
