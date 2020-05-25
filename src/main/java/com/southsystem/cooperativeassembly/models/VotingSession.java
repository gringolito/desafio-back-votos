package com.southsystem.cooperativeassembly.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "voting_sessions")
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voting_session_id")
    private Long votingSessionId;

    @NotNull
    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @NotNull
    private LocalDateTime expires;

    @OneToMany(mappedBy = "votingSession")
    private List<Vote> votes;
}
