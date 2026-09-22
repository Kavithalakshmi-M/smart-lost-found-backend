package com.smartlostfound.claimservice.dto;

import java.time.LocalDateTime;

import com.smartlostfound.claimservice.entity.Claim.ClaimStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimResponse {

    private Long id;

    private Long itemId;

    private Long claimantId;

    private String description;

    private ClaimStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}