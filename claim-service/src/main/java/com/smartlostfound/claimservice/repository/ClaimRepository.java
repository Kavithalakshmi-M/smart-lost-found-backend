package com.smartlostfound.claimservice.repository;

import com.smartlostfound.claimservice.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClaimRepository extends JpaRepository<Claim, Long> {

    List<Claim> findByClaimantId(Long claimantId);

    List<Claim> findByItemId(Long itemId);

    boolean existsByItemIdAndClaimantId(
            Long itemId,
            Long claimantId
    );
}