package edu.gmu.cs.hearts.domain.card;

import edu.gmu.cs.hearts.domain.Player;
import edu.gmu.cs.hearts.game.GamePlayer;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Game {

    @Id
    private UUID gameID;

    @OneToMany(mappedBy = "email", cascade = CascadeType.ALL)
    private List<Player> playerList;



}
