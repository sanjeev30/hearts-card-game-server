package edu.gmu.cs.hearts.domain;

import edu.gmu.cs.hearts.domain.id.HandWinnerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@IdClass(HandWinnerId.class)
public class HandWinner {

    @Id
    private Integer gameId;

    @Id
    private Integer moveNo;

    private Integer playerId;

    private Integer points;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "playerId", updatable = false, insertable = false, nullable = false)
    private Player player;
}
