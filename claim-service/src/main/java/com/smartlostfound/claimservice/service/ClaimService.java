package com.smartlostfound.claimservice.service;

import com.smartlostfound.claimservice.dto.ClaimRequest;
import com.smartlostfound.claimservice.dto.ClaimResponse;
import com.smartlostfound.claimservice.entity.Claim;
import com.smartlostfound.claimservice.exception.InvalidClaimException;
import com.smartlostfound.claimservice.exception.ResourceNotFoundException;
import com.smartlostfound.claimservice.repository.ClaimRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ClaimService {

    private final ClaimRepository claimRepository;

    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    // ============================================================
    // CREATE CLAIM
    // ============================================================

    public ClaimResponse createClaim(
            ClaimRequest request,
            Long claimantId) {

        if (claimantId == null) {
            throw new InvalidClaimException(
                    "Claimant ID is required"
            );
        }

        if (request == null || request.getItemId() == null) {
            throw new InvalidClaimException(
                    "Item ID is required"
            );
        }

        // Prevent duplicate claims from the same user
        // for the same item.
        if (claimRepository.existsByItemIdAndClaimantId(
                request.getItemId(),
                claimantId)) {

            throw new InvalidClaimException(
                    "You have already submitted a claim for this item"
            );
        }

        Claim claim = Claim.builder()
                .itemId(request.getItemId())
                .claimantId(claimantId)
                .description(request.getDescription())
                .status(Claim.ClaimStatus.PENDING)
                .build();

        Claim savedClaim = claimRepository.save(claim);

        return mapToResponse(savedClaim);
    }

    // ============================================================
    // GET CLAIM BY ID
    // ============================================================

    @Transactional(readOnly = true)
    public ClaimResponse getClaimById(Long id) {

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + id
                        )
                );

        return mapToResponse(claim);
    }

    // ============================================================
    // GET CLAIMS OF A USER
    // ============================================================

    @Transactional(readOnly = true)
    public List<ClaimResponse> getMyClaims(Long claimantId) {

        if (claimantId == null) {
            throw new InvalidClaimException(
                    "Claimant ID is required"
            );
        }

        return claimRepository.findByClaimantId(claimantId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ============================================================
    // GET CLAIMS FOR AN ITEM
    // ============================================================

    @Transactional(readOnly = true)
    public List<ClaimResponse> getClaimsByItem(Long itemId) {

        if (itemId == null) {
            throw new InvalidClaimException(
                    "Item ID is required"
            );
        }

        return claimRepository.findByItemId(itemId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ============================================================
    // UPDATE CLAIM STATUS
    // ============================================================

    public ClaimResponse updateStatus(
            Long id,
            Claim.ClaimStatus status) {

        if (status == null) {
            throw new InvalidClaimException(
                    "Claim status is required"
            );
        }

        Claim claim = claimRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Claim not found with ID: " + id
                        )
                );

        // Don't update if the status is already the same.
        if (claim.getStatus() == status) {
            return mapToResponse(claim);
        }

        claim.setStatus(status);

        Claim updatedClaim = claimRepository.save(claim);

        return mapToResponse(updatedClaim);
    }

    // ============================================================
    // DELETE CLAIM
    // ============================================================

    public void deleteClaim(Long id) {

        if (!claimRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Claim not found with ID: " + id
            );
        }

        claimRepository.deleteById(id);
    }

    // ============================================================
    // ENTITY → RESPONSE DTO
    // ============================================================

    private ClaimResponse mapToResponse(Claim claim) {

        return ClaimResponse.builder()
                .id(claim.getId())
                .itemId(claim.getItemId())
                .claimantId(claim.getClaimantId())
                .description(claim.getDescription())
                .status(claim.getStatus())
                .createdAt(claim.getCreatedAt())
                .updatedAt(claim.getUpdatedAt())
                .build();
    }
}