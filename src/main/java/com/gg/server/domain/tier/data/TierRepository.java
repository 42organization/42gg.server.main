package com.gg.server.domain.tier.data;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TierRepository extends JpaRepository<Tier, Long> {

	@Query("SELECT t FROM Tier t WHERE t.id = (SELECT MIN(t.id) FROM Tier t)")
	Optional<Tier> findStartTier();

	Optional<Tier> findByName(String name);
}
