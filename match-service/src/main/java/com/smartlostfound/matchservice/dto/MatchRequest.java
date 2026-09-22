package com.smartlostfound.matchservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MatchRequest {

    @NotNull(message = "Lost item ID is required")
    private Long lostItemId;

    @NotNull(message = "Found item ID is required")
    private Long foundItemId;
}