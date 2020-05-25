package com.southsystem.cooperativeassembly.repositories;

import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.models.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Long countVotesByVotingSessionAndVote(VotingSession session, String vote);

    Vote findFirstByVotingSessionAndCpf(VotingSession session, String cpf);
}
