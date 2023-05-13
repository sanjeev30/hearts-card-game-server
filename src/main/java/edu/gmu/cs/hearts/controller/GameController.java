package edu.gmu.cs.hearts.controller;

import edu.gmu.cs.hearts.domain.Game;
import edu.gmu.cs.hearts.domain.card.Card;
import edu.gmu.cs.hearts.domain.card.Deck;
import edu.gmu.cs.hearts.model.GameInstance;
import edu.gmu.cs.hearts.model.GameRequest;
import edu.gmu.cs.hearts.model.PlayCardRequest;
import edu.gmu.cs.hearts.service.GameService;
import edu.gmu.cs.hearts.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<GameInstance> joinGame(@RequestHeader Map<String, String> header, @RequestBody GameRequest request) throws Exception {
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

    @PostMapping("/gamePoll")
    public ResponseEntity<GameInstance> gamePoll(@RequestHeader Map<String, String> header, @RequestBody GameRequest gameRequest) {
        String token = header.get("authorization").substring(7);
        String email = jwtService.extractPlayerEmail(token);
        return ResponseEntity.ok(gameService.gameUpdates(email, gameRequest.getGameId()));
    }
    @PostMapping("/startGame")
    public ResponseEntity<GameInstance> startGame(@RequestHeader Map<String, String> header, @RequestBody GameRequest gameRequest) {
        String token = header.get("authorization").substring(7);
        String email = jwtService.extractPlayerEmail(token);
        return ResponseEntity.ok(gameService.startGame(email, gameRequest.getGameId()));
    }

    @PostMapping("/playCard")
    public ResponseEntity<GameInstance> playCard(@RequestHeader Map<String, String> header, @RequestBody PlayCardRequest playCardRequest) throws Exception {
        String token = header.get("authorization").substring(7);
        String email = jwtService.extractPlayerEmail(token);
        return ResponseEntity.ok(gameService.playCard(email, playCardRequest.getGameId(), playCardRequest.getCard()));
    }
}