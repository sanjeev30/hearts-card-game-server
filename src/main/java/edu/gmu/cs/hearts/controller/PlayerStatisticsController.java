package edu.gmu.cs.hearts.controller;

import edu.gmu.cs.hearts.domain.PlayerStatistics;
import edu.gmu.cs.hearts.service.JWTService;
import edu.gmu.cs.hearts.service.PlayerStatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/stat")
@Slf4j
@RequiredArgsConstructor
public class PlayerStatisticsController {

    private final JWTService jwtService;

    private final PlayerStatisticsService playerStatisticsService;

    @GetMapping
    public PlayerStatistics getPlayerStatistics(@RequestHeader Map<String, String> header) {
        String token = header.get("authorization").substring(7);
        return playerStatisticsService.getPlayerStats(token);
    }
}
