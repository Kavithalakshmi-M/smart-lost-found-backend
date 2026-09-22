package com.smartlostfound.matchservice.dto;

import com.smartlostfound.matchservice.entity.Match;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class MatchResponse {

    private Long id;
    private Long lostItemId;
    private Long foundItemId;
    private Double similarityScore;
    private Match.MatchStatus status;
    private LocalDateTime createdAt;
}