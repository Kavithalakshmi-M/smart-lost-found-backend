package com.smartlostfound.itemservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "ITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private LocalDateTime dateReported;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ItemStatus status;

    @Column(nullable = false)
    private Long reportedBy;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum ItemType {
        LOST,
        FOUND
    }

    public enum ItemStatus {
        ACTIVE,
        CLAIMED,
        RESOLVED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (dateReported == null) {
            dateReported = LocalDateTime.now();
        }

        if (status == null) {
            status = ItemStatus.ACTIVE;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}