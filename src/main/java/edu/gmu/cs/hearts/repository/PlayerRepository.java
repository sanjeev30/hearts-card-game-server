package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Integer> {

    Optional<Player> findByEmail(String email);
}
