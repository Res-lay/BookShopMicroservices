package com.bookshop.inventoryservice.service;

import com.bookshop.inventoryservice.dto.InventoryRequest;
import com.bookshop.inventoryservice.dto.InventoryResponse;
import com.bookshop.inventoryservice.model.Inventory;
import com.bookshop.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<InventoryResponse> invent = inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()).toList();
        invent.forEach(System.out::println);
        return invent;
    }

    public void setInventoryItem(InventoryRequest inventoryRequest){
        Inventory inventory = new Inventory();
        inventory.setQuantity(inventoryRequest.getQuantity());
        inventory.setSkuCode(inventoryRequest.getSkuCode());
        inventoryRepository.save(inventory);
        log.info("New inventory item saved with id = {}", inventory.getId());
    }

    public void reduceQuantity(List<InventoryRequest> inventoryRequests) {
        for (InventoryRequest request : inventoryRequests) {
            Optional<Inventory> inventoryOptional = inventoryRepository.findBySkuCode(request.getSkuCode());
            Inventory inventory = inventoryOptional.get();
            inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
            inventoryRepository.save(inventory);
        }
    }

}
