package edu.gmu.cs.hearts.service;

import edu.gmu.cs.hearts.domain.Player;
import edu.gmu.cs.hearts.domain.PlayerStatistics;
import edu.gmu.cs.hearts.repository.PlayerRepository;
import edu.gmu.cs.hearts.repository.PlayerStatisticsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlayerStatisticsService {

    private final JWTService jwtService;
    private final PlayerRepository playerRepository;
    private final PlayerStatisticsRepository playerStatisticsRepository;

    public PlayerStatistics getPlayerStats(String jwtToken) {
        String playerEmail = jwtService.extractPlayerEmail(jwtToken);
        Player player = playerRepository.findByEmail(playerEmail).get();
        PlayerStatistics response = playerStatisticsRepository.findById(player.getId()).get();
        return response;
    }
}
