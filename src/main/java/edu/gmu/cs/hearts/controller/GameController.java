package edu.gmu.cs.hearts.controller;

import edu.gmu.cs.hearts.domain.Game;
import edu.gmu.cs.hearts.model.GameInstance;
import edu.gmu.cs.hearts.model.JoinGameRequest;
import edu.gmu.cs.hearts.service.GameService;
import edu.gmu.cs.hearts.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final JWTService jwtService;

    @PostMapping("/create")
    public ResponseEntity<Game> createNewGame() {
        return ResponseEntity.ok(gameService.createGame());
    }

    @PostMapping("/join")
    public ResponseEntity<GameInstance> joinGame(@RequestHeader Map<String, String> header, @RequestBody JoinGameRequest request) throws Exception {
        String token = header.get("authorization").substring(7);
        String email = jwtService.extractPlayerEmail(token);
        return ResponseEntity.ok(gameService.joinGame(email, request.getGameId()));
    }

    @PostMapping("/joinAnyGame")
    public ResponseEntity<GameInstance> joinAnyGame(@RequestHeader Map<String, String> header) throws Exception {
        String token = header.get("authorization").substring(7);
        String email = jwtService.extractPlayerEmail(token);
        return ResponseEntity.ok(gameService.joinAnyGame(email));
    }
}