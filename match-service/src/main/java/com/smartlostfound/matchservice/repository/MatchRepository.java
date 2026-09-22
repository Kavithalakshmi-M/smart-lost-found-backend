package com.smartlostfound.matchservice.repository;

import com.smartlostfound.matchservice.entity.Match;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchRepository extends JpaRepository<Match, Long> {

    List<Match> findByLostItemId(Long lostItemId);

    List<Match> findByFoundItemId(Long foundItemId);

    List<Match> findByStatus(Match.MatchStatus status);
}