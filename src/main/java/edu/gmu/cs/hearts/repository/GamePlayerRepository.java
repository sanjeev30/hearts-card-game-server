package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.GamePlayer;
import edu.gmu.cs.hearts.domain.id.GamePlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, GamePlayerId> {
    Integer countGamePlayerByGameIdEquals(Integer gameId);

    List<GamePlayer> findGamePlayerByGameId(Integer gameId);
}
