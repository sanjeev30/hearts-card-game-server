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
    private Player northPlayer;
    private Player eastPlayer;
    private Player southPlayer;
    private Player westPlayer;
    private Status gameStatus;

}
