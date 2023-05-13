package edu.gmu.cs.hearts.domain;

import edu.gmu.cs.hearts.domain.card.Suit;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    @GeneratedValue
    private Integer id;

    private Date startTime;

    private Date endTime;

    @Enumerated(EnumType.STRING)
    private Status status;

    private Boolean heartBreak;

    @Enumerated(EnumType.ORDINAL)
    private PlayerDirection currentDirection;

    @Enumerated(EnumType.STRING)
    private Suit currentSuit;

}
