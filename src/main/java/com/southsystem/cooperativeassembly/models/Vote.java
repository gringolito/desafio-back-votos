package com.southsystem.cooperativeassembly.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity(name = "votes")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"cpf", "voting_session_id"}, name = "per_session_unique"))
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vote_id")
    private Long voteId;

    @NotNull
    @Column(name = "cpf")
    private String cpf;

    @NotNull
    @Pattern(regexp = "Sim|Não")
    private String vote;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "voting_session_id")
    private VotingSession votingSession;
}
