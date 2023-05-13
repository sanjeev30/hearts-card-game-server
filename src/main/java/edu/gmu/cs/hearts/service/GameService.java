package edu.gmu.cs.hearts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.gmu.cs.hearts.domain.*;
import edu.gmu.cs.hearts.domain.card.Card;
import edu.gmu.cs.hearts.domain.card.Deck;
import edu.gmu.cs.hearts.model.GameInstance;
import edu.gmu.cs.hearts.model.PlayerInstance;
import edu.gmu.cs.hearts.repository.GamePlayerRepository;
import edu.gmu.cs.hearts.repository.GameRepository;
import edu.gmu.cs.hearts.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                .currentDirection(PlayerDirection.NORTH)
                .heartBreak(false)
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
        return getGameInstance(playerEmail, gameId);
    }

    public GameInstance getGameInstance(String email, Integer gameId) {
        GameInstance gameInstance = new GameInstance();
        Game game = gameRepository.findById(gameId).get();
        gameInstance.setGameId(game.getId());
        List<GamePlayer> players = gamePlayerRepository.findGamePlayerByGameId(gameId);
        for(GamePlayer player: players) {
            Player playerData = playerRepository.findById(player.getPlayerId()).get();
            PlayerInstance playerInGame = PlayerInstance.builder()
                    .firstName(playerData.getFirstName())
                    .lastName(playerData.getLastName())
                    .playerId(playerData.getId())
                    .score(player.getScore())
                    .build();
            if(playerData.getEmail().equals(email)) {
                gameInstance.setCards(player.getCards());
                gameInstance.setPlayerId(playerData.getId());
            }
            if(game.getCurrentDirection().equals(player.getDirection())) {
                playerInGame.setCurrentPlayer(true);
            }
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

    public GameInstance gameUpdates(String email, Integer gameId) {
        return getGameInstance(email, gameId);
    }

    public GameInstance startGame(String email, Integer gameId) {
        Game game = gameRepository.findById(gameId).get();
        game.setStatus(Status.IN_PROGRESS);
        gameRepository.save(game);
        List<GamePlayer> gamePlayers = gamePlayerRepository.findGamePlayerByGameId(game.getId());
        Deck deck = new Deck();
        List<Card> cards = deck.getCards();
        for(GamePlayer player: gamePlayers) {
            if(player.getDirection().equals(PlayerDirection.NORTH)) {
                player.setCards(getCards(0, 13, cards));
            } else if(player.getDirection().equals(PlayerDirection.EAST)) {
                player.setCards(getCards(13, 26, cards));
            } else if(player.getDirection().equals(PlayerDirection.SOUTH)) {
                player.setCards(getCards(26, 39, cards));
            } else if(player.getDirection().equals(PlayerDirection.WEST)) {
                player.setCards(getCards(39, 52, cards));
            }
            GamePlayer savedPlayer = gamePlayerRepository.save(player);
        }
        return getGameInstance(email, gameId);
    }

    private String getCards(int startIndex, int endIndex, List<Card> cards) {
        List<String> playerCards = new ArrayList<>();
        for(int i=startIndex; i<endIndex; i++) {
            playerCards.add(cards.get(i).getSuit() + "-" + cards.get(i).getRank());
        }
        return String.join("," , playerCards);
    }

}
