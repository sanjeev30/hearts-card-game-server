package edu.gmu.cs.hearts.repository;

import edu.gmu.cs.hearts.domain.PlayerStatistics;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerStatisticsRepository extends JpaRepository<PlayerStatistics, Integer> {
}
