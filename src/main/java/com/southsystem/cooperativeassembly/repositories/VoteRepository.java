package com.southsystem.cooperativeassembly.repositories;

import com.southsystem.cooperativeassembly.models.Vote;
import com.southsystem.cooperativeassembly.models.VotingSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    public Long countVotesByVotingSessionAndVote(VotingSession session, String vote);

    public Vote findFirstByVotingSessionAndAssociateCpf(VotingSession session, String cpf);
}
