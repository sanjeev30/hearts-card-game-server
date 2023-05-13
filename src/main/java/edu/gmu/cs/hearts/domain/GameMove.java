package edu.gmu.cs.hearts.domain;

import edu.gmu.cs.hearts.domain.id.GameMoveId;
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
@IdClass(GameMoveId.class)
public class GameMove {

    @Id
    private Integer moveNo;

    @Id
    private Integer gameId;

    @Id
    private Integer playerId;

    private String cardPlayed;

    private Integer movePoints;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "gameId", updatable = false, insertable = false, nullable = false)
    private Game game;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "playerId", updatable = false, insertable = false, nullable = false)
    private Player player;

}
