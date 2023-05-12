package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.Game;
import edu.gmu.cs.hearts.domain.Status;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameRepository extends JpaRepository<Game, Integer> {

    List<Game> findGamesByStatusEqualsOrderByStartTimeDesc(Status status);
}
