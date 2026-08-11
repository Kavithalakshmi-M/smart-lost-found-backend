package com.smartlostfound.claimservice.controller;

import com.smartlostfound.claimservice.dto.ClaimRequest;
import com.smartlostfound.claimservice.dto.ClaimResponse;
import com.smartlostfound.claimservice.entity.Claim;
import com.smartlostfound.claimservice.service.ClaimService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/claims")
public class ClaimController {

    private final ClaimService claimService;

    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }

    // ============================================================
    // CREATE CLAIM
    // ============================================================

    @PostMapping
    public ResponseEntity<ClaimResponse> createClaim(
            @Valid @RequestBody ClaimRequest request,
            Authentication authentication) {

        Long claimantId = getAuthenticatedUserId(authentication);

        ClaimResponse response =
                claimService.createClaim(
                        request,
                        claimantId
                );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    // ============================================================
    // GET CLAIM BY ID
    // ============================================================

    @GetMapping("/{id}")
    public ResponseEntity<ClaimResponse> getClaimById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                claimService.getClaimById(id)
        );
    }

    // ============================================================
    // GET MY CLAIMS
    // ============================================================

    @GetMapping("/my")
    public ResponseEntity<List<ClaimResponse>> getMyClaims(
            Authentication authentication) {

        Long claimantId = getAuthenticatedUserId(authentication);

        return ResponseEntity.ok(
                claimService.getMyClaims(claimantId)
        );
    }

    // ============================================================
    // GET CLAIMS FOR AN ITEM
    // ============================================================

    @GetMapping("/item/{itemId}")
    public ResponseEntity<List<ClaimResponse>> getClaimsByItem(
            @PathVariable Long itemId) {

        return ResponseEntity.ok(
                claimService.getClaimsByItem(itemId)
        );
    }

    // ============================================================
    // UPDATE CLAIM STATUS
    // ============================================================

    @PatchMapping("/{id}/status")
    public ResponseEntity<ClaimResponse> updateStatus(
            @PathVariable Long id,
            @RequestParam Claim.ClaimStatus status) {

        return ResponseEntity.ok(
                claimService.updateStatus(id, status)
        );
    }

    // ============================================================
    // DELETE CLAIM
    // ============================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClaim(
            @PathVariable Long id) {

        claimService.deleteClaim(id);

        return ResponseEntity
                .noContent()
                .build();
    }

    // ============================================================
    // GET AUTHENTICATED USER ID
    // ============================================================

    private Long getAuthenticatedUserId(
            Authentication authentication) {

        return (Long) authentication.getPrincipal();
    }
}