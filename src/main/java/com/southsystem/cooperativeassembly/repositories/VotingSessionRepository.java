package com.southsystem.cooperativeassembly.repositories;

import com.southsystem.cooperativeassembly.models.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotingSessionRepository extends JpaRepository<VotingSession, Long> {
}
