package com.smartlostfound.itemservice.dto;

import com.smartlostfound.itemservice.entity.Item;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ItemRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @NotBlank(message = "Category is required")
    private String category;

    @NotBlank(message = "Location is required")
    private String location;

    private LocalDateTime dateReported;

    private String imageUrl;

    @NotNull(message = "Item type is required")
    private Item.ItemType type;
}