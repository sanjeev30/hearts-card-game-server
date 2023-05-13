package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.Game;
import edu.gmu.cs.hearts.domain.GameMove;
import edu.gmu.cs.hearts.domain.id.GameMoveId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GameMoveRepository extends JpaRepository<GameMove, GameMoveId> {

    Integer countAllByGameIdAndPlayerId(Integer gameId, Integer playerId);

    @Query(value = "SELECT max(moveNo) FROM GameMove WHERE gameId = ?1")
    Integer maxMoveNumber(Integer gameId);

    List<GameMove> getGameMovesByGameIdAndMoveNo(Integer gameId, Integer moveNo);

    List<GameMove> getGameMovesByGameIdAndPlayerId(Integer gameId, Integer playerId);
}
