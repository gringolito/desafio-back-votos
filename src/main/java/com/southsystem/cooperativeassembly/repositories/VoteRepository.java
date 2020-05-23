package com.southsystem.cooperativeassembly.repositories;

import com.southsystem.cooperativeassembly.models.Vote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
}
