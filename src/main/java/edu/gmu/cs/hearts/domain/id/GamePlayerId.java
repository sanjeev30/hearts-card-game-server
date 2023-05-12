package edu.gmu.cs.hearts.domain.id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayerId implements Serializable {
    private Integer gameId;
    private Integer playerId;
}
