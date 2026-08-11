package com.smartlostfound.claimservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CLAIMS")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long itemId;

    @Column(nullable = false)
    private Long claimantId;

    @Column(length = 2000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ClaimStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public enum ClaimStatus {
        PENDING,
        APPROVED,
        REJECTED
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (status == null) {
            status = ClaimStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}