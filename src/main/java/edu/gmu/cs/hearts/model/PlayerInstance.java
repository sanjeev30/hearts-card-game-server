package edu.gmu.cs.hearts.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
