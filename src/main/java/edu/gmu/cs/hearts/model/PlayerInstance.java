package edu.gmu.cs.hearts.model;

import edu.gmu.cs.hearts.domain.GameMove;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlayerInstance {
    private Integer playerId;
    private String firstName;
    private String lastName;
    private String cards;
    private Integer score;
    private Boolean passing;
    private Boolean currentPlayer;
    private List<GameMove> gameMoves;
}
