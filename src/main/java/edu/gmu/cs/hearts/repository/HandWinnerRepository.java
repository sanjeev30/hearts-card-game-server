package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.HandWinner;
import edu.gmu.cs.hearts.domain.id.HandWinnerId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HandWinnerRepository extends JpaRepository<HandWinner, HandWinnerId> {

    List<HandWinner> getHandWinnersByGameId(Integer gameId);
}
