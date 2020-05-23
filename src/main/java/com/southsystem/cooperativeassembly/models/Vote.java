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
    private Long Id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "id")
    private Associate associate;

    @Pattern(regexp = "Sim|Não")
    private String vote;

    @ManyToOne()
    @JoinColumn(name = "id")
    @JsonIgnore
    private VotingSession votingSession;
}
