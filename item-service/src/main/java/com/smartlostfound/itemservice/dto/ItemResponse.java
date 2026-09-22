package com.smartlostfound.itemservice.dto;

import com.smartlostfound.itemservice.entity.Item;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemResponse {

    private Long id;
    private String title;
    private String description;
    private String category;
    private String location;
    private LocalDateTime dateReported;
    private String imageUrl;
    private Item.ItemType type;
    private Item.ItemStatus status;
    private Long reportedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}