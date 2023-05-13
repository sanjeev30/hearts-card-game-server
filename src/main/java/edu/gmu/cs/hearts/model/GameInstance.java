package edu.gmu.cs.hearts.model;

import edu.gmu.cs.hearts.domain.Player;
import edu.gmu.cs.hearts.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameInstance {
    private Integer gameId;
    private Integer playerId;
    private PlayerInstance northPlayer;
    private PlayerInstance eastPlayer;
    private PlayerInstance southPlayer;
    private PlayerInstance westPlayer;
    private Status gameStatus;
    private boolean heartBreak;
    private String cards;

}
