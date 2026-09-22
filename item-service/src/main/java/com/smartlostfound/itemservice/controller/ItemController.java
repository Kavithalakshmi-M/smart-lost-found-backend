package com.smartlostfound.itemservice.controller;

import com.smartlostfound.itemservice.dto.ItemRequest;
import com.smartlostfound.itemservice.dto.ItemResponse;
import com.smartlostfound.itemservice.entity.Item;
import com.smartlostfound.itemservice.service.ItemService;
import org.springframework.security.core.Authentication;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/items")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ResponseEntity<ItemResponse> createItem(
            @Valid @RequestBody ItemRequest request,
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        ItemResponse response =
                itemService.createItem(request, userId);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponse> getItemById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                itemService.getItemById(id)
        );
    }

    @GetMapping
    public ResponseEntity<List<ItemResponse>> getAllItems() {

        return ResponseEntity.ok(
                itemService.getAllItems()
        );
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ItemResponse>> getItemsByType(
            @PathVariable Item.ItemType type) {

        return ResponseEntity.ok(
                itemService.getItemsByType(type)
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<ItemResponse>> getItemsByStatus(
            @PathVariable Item.ItemStatus status) {

        return ResponseEntity.ok(
                itemService.getItemsByStatus(status)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ItemResponse>> getItemsByUser(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                itemService.getItemsByUser(userId)
        );
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ItemResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Item.ItemStatus status) {

        return ResponseEntity.ok(
                itemService.updateStatus(id, status)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id) {

        itemService.deleteItem(id);

        return ResponseEntity.noContent().build();
    }
}