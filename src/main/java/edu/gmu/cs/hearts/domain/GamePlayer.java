package edu.gmu.cs.hearts.domain;

import edu.gmu.cs.hearts.domain.id.GamePlayerId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(GamePlayerId.class)
public class GamePlayer {

    @Id
    private Integer gameId;

    @Id
    private Integer playerId;

    @ManyToOne
    @JoinColumn(referencedColumnName = "id", name = "gameId", updatable = false, insertable = false, nullable = false)
    private Game game;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", name = "playerId", updatable = false, insertable = false, nullable = false)
    private Player player;

    @Enumerated(EnumType.ORDINAL)
    private PlayerDirection direction;
}
