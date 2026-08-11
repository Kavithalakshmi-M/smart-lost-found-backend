package com.smartlostfound.itemservice.service;

import com.smartlostfound.itemservice.dto.ItemRequest;
import com.smartlostfound.itemservice.dto.ItemResponse;
import com.smartlostfound.itemservice.entity.Item;
import com.smartlostfound.itemservice.exception.InvalidItemStatusException;
import com.smartlostfound.itemservice.exception.ItemNotFoundException;
import com.smartlostfound.itemservice.repository.ItemRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Create a new item
    public ItemResponse createItem(
            ItemRequest request,
            Long userId) {

        Item item = new Item();

        item.setTitle(request.getTitle());
        item.setDescription(request.getDescription());
        item.setCategory(request.getCategory());
        item.setLocation(request.getLocation());
        item.setDateReported(request.getDateReported());
        item.setImageUrl(request.getImageUrl());
        item.setType(request.getType());
        item.setStatus(Item.ItemStatus.ACTIVE);
        item.setReportedBy(userId);

        Item savedItem = itemRepository.save(item);

        return convertToResponse(savedItem);
    }

    // Get item by ID
    public ItemResponse getItemById(Long id) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new ItemNotFoundException(
                                "Item not found with id: " + id
                        )
                );

        return convertToResponse(item);
    }

    // Get all items
    public List<ItemResponse> getAllItems() {

        return itemRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get items by type
    public List<ItemResponse> getItemsByType(
            Item.ItemType type) {

        return itemRepository.findByType(type)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get items by status
    public List<ItemResponse> getItemsByStatus(
            Item.ItemStatus status) {

        return itemRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Get items reported by a specific user
    public List<ItemResponse> getItemsByUser(Long userId) {

        return itemRepository.findByReportedBy(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // Update item status
    public ItemResponse updateStatus(
            Long id,
            Item.ItemStatus status) {

        Item item = itemRepository.findById(id)
                .orElseThrow(() ->
                        new ItemNotFoundException(
                                "Item not found with id: " + id
                        )
                );

        Item.ItemStatus currentStatus = item.getStatus();

        // A resolved item cannot be modified
        if (currentStatus == Item.ItemStatus.RESOLVED) {
            throw new InvalidItemStatusException(
                    "Resolved items cannot be updated"
            );
        }

        // A claimed item cannot be changed back to ACTIVE
        if (currentStatus == Item.ItemStatus.CLAIMED
                && status == Item.ItemStatus.ACTIVE) {

            throw new InvalidItemStatusException(
                    "A claimed item cannot be changed back to ACTIVE"
            );
        }

        item.setStatus(status);

        return convertToResponse(
                itemRepository.save(item)
        );
    }

    // Delete item
    public void deleteItem(Long id) {

        if (!itemRepository.existsById(id)) {
            throw new ItemNotFoundException(
                    "Item not found with id: " + id
            );
        }

        itemRepository.deleteById(id);
    }

    // Convert Entity → DTO
    private ItemResponse convertToResponse(Item item) {

        return new ItemResponse(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getCategory(),
                item.getLocation(),
                item.getDateReported(),
                item.getImageUrl(),
                item.getType(),
                item.getStatus(),
                item.getReportedBy(),
                item.getCreatedAt(),
                item.getUpdatedAt()
        );
    }
}