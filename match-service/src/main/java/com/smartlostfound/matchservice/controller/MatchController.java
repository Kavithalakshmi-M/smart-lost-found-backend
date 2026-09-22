package com.smartlostfound.matchservice.controller;

import com.smartlostfound.matchservice.dto.MatchRequest;
import com.smartlostfound.matchservice.dto.MatchResponse;
import com.smartlostfound.matchservice.entity.Match;
import com.smartlostfound.matchservice.service.MatchService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {

    private final MatchService matchService;

    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    // ============================================================
    // CREATE MATCH
    // ============================================================

    @PostMapping
    public ResponseEntity<MatchResponse> createMatch(
            @Valid @RequestBody MatchRequest request) {

        MatchResponse response =
                matchService.createMatch(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET MATCH BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<MatchResponse> getMatchById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                matchService.getMatchById(id)
        );
    }

    // ============================================================
    // GET MATCHES FOR LOST ITEM
    // ============================================================

    @GetMapping("/lost/{lostItemId}")
    public ResponseEntity<List<MatchResponse>> getMatchesByLostItem(
            @PathVariable Long lostItemId) {

        return ResponseEntity.ok(
                matchService.getMatchesByLostItem(lostItemId)
        );
    }

    // ============================================================
    // GET MATCHES FOR FOUND ITEM
    // ============================================================

    @GetMapping("/found/{foundItemId}")
    public ResponseEntity<List<MatchResponse>> getMatchesByFoundItem(
            @PathVariable Long foundItemId) {

        return ResponseEntity.ok(
                matchService.getMatchesByFoundItem(foundItemId)
        );
    }

    // ============================================================
    // GET MATCHES BY STATUS
    // ============================================================

    @GetMapping("/status/{status}")
    public ResponseEntity<List<MatchResponse>> getMatchesByStatus(
            @PathVariable Match.MatchStatus status) {

        return ResponseEntity.ok(
                matchService.getMatchesByStatus(status)
        );
    }

    // ============================================================
    // UPDATE MATCH STATUS
    // ============================================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<MatchResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Match.MatchStatus status) {

        return ResponseEntity.ok(
                matchService.updateStatus(id, status)
        );
    }
    @GetMapping("/test")
    public String test() {
        return "Match service is working";
    }

    // ============================================================
    // DELETE MATCH
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMatch(
            @PathVariable Long id) {

        matchService.deleteMatch(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}