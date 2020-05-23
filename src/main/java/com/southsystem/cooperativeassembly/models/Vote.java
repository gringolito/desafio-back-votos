package com.southsystem.cooperativeassembly.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vote_id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "associate_id")
    private Associate associate;

    @Pattern(regexp = "Sim|Não")
    private String vote;

    @ManyToOne()
    @JoinColumn(name = "voting_session_id")
    @JsonIgnore
    private VotingSession voting_session;
}
