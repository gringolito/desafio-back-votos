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
    private Long voting_session_id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id")
    private Topic topic;

    @NotNull
    private LocalDateTime expires;

    @OneToMany(mappedBy = "voting_session")
    private List<Vote> votes;
}
