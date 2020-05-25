package com.southsystem.cooperativeassembly.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@Entity(name = "votes")
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"associate_cpf", "voting_session_id"}, name = "per_session_unique"))
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vote_id;

    @NotNull
    @Column(name = "associate_cpf")
    private String associateCpf;

    @NotNull
    @Pattern(regexp = "Sim|Não")
    private String vote;

    @NotNull
    @ManyToOne()
    @JoinColumn(name = "voting_session_id")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private VotingSession votingSession;
}
