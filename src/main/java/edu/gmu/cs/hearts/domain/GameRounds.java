package edu.gmu.cs.hearts.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GameRounds {
    @Id
    private Integer id;

    private Integer gameId;

    private Integer roundNo;

}
