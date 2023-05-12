package edu.gmu.cs.hearts.service;

import edu.gmu.cs.hearts.domain.*;
import edu.gmu.cs.hearts.domain.id.GamePlayerId;
import edu.gmu.cs.hearts.model.GameInstance;
import edu.gmu.cs.hearts.repository.GamePlayerRepository;
import edu.gmu.cs.hearts.repository.GameRepository;
import edu.gmu.cs.hearts.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final PlayerRepository playerRepository;

    public Game createGame() {
        var game = Game.builder()
                        .startTime(new Date(System.currentTimeMillis()))
                        .status(Status.WAITING)
                        .build();
        return gameRepository.save(game);
    }

    public GameInstance joinGame(String playerEmail, Integer gameId) throws Exception {
        Integer count = gamePlayerRepository.countGamePlayerByGameIdEquals(gameId);
        if(count >= 4) {
            throw new Exception("Cannot join the room! Room full");
        }
        Player player = playerRepository.findByEmail(playerEmail).orElseThrow(Exception::new);
        PlayerDirection direction = PlayerDirection.NORTH;
        if (count == 3) {
            direction = PlayerDirection.WEST;
        } else if(count == 2) {
            direction = PlayerDirection.SOUTH;
        } else if(count == 1) {
            direction = PlayerDirection.EAST;
        }
        GamePlayer gamePlayer = GamePlayer.builder()
                        .playerId(player.getId())
                        .gameId(gameId)
                        .direction(direction)
                        .build();
        GamePlayer joinedPlayer = gamePlayerRepository.save(gamePlayer);
        if(joinedPlayer.getDirection().equals(PlayerDirection.WEST)) {
            Game game = gameRepository.findById(gameId).get();
            game.setStatus(Status.READY);
            gameRepository.save(game);
        }
        log.info("Joined", joinedPlayer.toString());
        return getGameInstance(gameId);
    }

    public GameInstance getGameInstance(Integer gameId) {
        GameInstance gameInstance = new GameInstance();
        Game game = gameRepository.findById(gameId).get();
        gameInstance.setGameId(game.getId());
        List<GamePlayer> players = gamePlayerRepository.findGamePlayerByGameId(gameId);
        for(GamePlayer player: players) {
            Player playerInGame = playerRepository.findById(player.getPlayerId()).get();
            if(player.getDirection().equals(PlayerDirection.NORTH)) {
                gameInstance.setNorthPlayer(playerInGame);
            } else if(player.getDirection().equals(PlayerDirection.EAST)) {
                gameInstance.setEastPlayer(playerInGame);
            } else if(player.getDirection().equals(PlayerDirection.SOUTH)) {
                gameInstance.setSouthPlayer(playerInGame);
            } else if(player.getDirection().equals(PlayerDirection.WEST)) {
                gameInstance.setWestPlayer(playerInGame);
            }
        }
        gameInstance.setGameStatus(game.getStatus());
        return gameInstance;
    }

    public GameInstance joinAnyGame(String email) throws Exception {
        List<Game> allWaitingGame = gameRepository.findGamesByStatusEqualsOrderByStartTimeDesc(Status.WAITING);
        Game game = allWaitingGame.stream().findFirst().get();
        return joinGame(email, game.getId());
    }
}
