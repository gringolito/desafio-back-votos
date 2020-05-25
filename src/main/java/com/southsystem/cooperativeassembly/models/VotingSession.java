package com.southsystem.cooperativeassembly.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "voting_sessions")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class VotingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voting_session_id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;

    @NotNull
    private LocalDateTime expires;

    @OneToMany(mappedBy = "votingSession")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<Vote> votes;
}
