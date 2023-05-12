package edu.gmu.cs.hearts.domain;

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
public class PlayerStatistics {

    @Id
    @Column(name = "player_id")
    private Integer playerId;
    
    private Integer gamesPlayed = 0;

    private Integer roundsWon = 0;

    private Integer roundsLost = 0;

    private Integer gamesWon = 0;

    private Integer gamesLost = 0;

    @OneToOne
    @JoinColumn(referencedColumnName = "id", nullable = false, insertable = false, updatable = false)
    private Player player;
}
