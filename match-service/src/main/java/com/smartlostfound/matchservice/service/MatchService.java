package com.smartlostfound.matchservice.service;

import com.smartlostfound.matchservice.dto.MatchRequest;
import com.smartlostfound.matchservice.dto.MatchResponse;
import com.smartlostfound.matchservice.entity.Match;
import com.smartlostfound.matchservice.repository.MatchRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {

    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    // ============================================================
    // CREATE MATCH
    // ============================================================

    public MatchResponse createMatch(MatchRequest request) {

        /*
         * Temporary rule-based similarity score.
         *
         * Later this method can call the Python AI service
         * to calculate an actual image/text similarity score.
         */

        double similarityScore = calculateSimilarity(
                request.getLostItemId(),
                request.getFoundItemId()
        );

        Match match = new Match();

        match.setLostItemId(request.getLostItemId());
        match.setFoundItemId(request.getFoundItemId());
        match.setSimilarityScore(similarityScore);
        match.setStatus(Match.MatchStatus.PENDING);

        Match savedMatch = matchRepository.save(match);

        return convertToResponse(savedMatch);
    }

    // ============================================================
    // GET MATCH BY ID
    // ============================================================

    public MatchResponse getMatchById(Long id) {

        Match match = matchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Match not found with id: " + id)
                );

        return convertToResponse(match);
    }

    // ============================================================
    // GET MATCHES FOR LOST ITEM
    // ============================================================

    public List<MatchResponse> getMatchesByLostItem(Long lostItemId) {

        return matchRepository.findByLostItemId(lostItemId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ============================================================
    // GET MATCHES FOR FOUND ITEM
    // ============================================================

    public List<MatchResponse> getMatchesByFoundItem(Long foundItemId) {

        return matchRepository.findByFoundItemId(foundItemId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ============================================================
    // GET MATCHES BY STATUS
    // ============================================================

    public List<MatchResponse> getMatchesByStatus(
            Match.MatchStatus status) {

        return matchRepository.findByStatus(status)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ============================================================
    // UPDATE MATCH STATUS
    // ============================================================

    public MatchResponse updateStatus(
            Long id,
            Match.MatchStatus status) {

        Match match = matchRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Match not found with id: " + id)
                );

        match.setStatus(status);

        Match updatedMatch = matchRepository.save(match);

        return convertToResponse(updatedMatch);
    }

    // ============================================================
    // DELETE MATCH
    // ============================================================

    public void deleteMatch(Long id) {

        if (!matchRepository.existsById(id)) {
            throw new RuntimeException(
                    "Match not found with id: " + id
            );
        }

        matchRepository.deleteById(id);
    }

    // ============================================================
    // TEMPORARY MATCHING LOGIC
    // ============================================================

    private double calculateSimilarity(
            Long lostItemId,
            Long foundItemId) {

        /*
         * Placeholder for the AI matching engine.
         *
         * For now, return a deterministic score so that
         * the complete Match Service can be tested.
         *
         * This will later be replaced by:
         *
         * Match Service
         *       ↓
         * Python AI Service
         *       ↓
         * Image/Text similarity
         *       ↓
         * similarity score
         */

        return 0.75;
    }

    // ============================================================
    // ENTITY → DTO
    // ============================================================

    private MatchResponse convertToResponse(Match match) {

        return new MatchResponse(
                match.getId(),
                match.getLostItemId(),
                match.getFoundItemId(),
                match.getSimilarityScore(),
                match.getStatus(),
                match.getCreatedAt()
        );
    }
}