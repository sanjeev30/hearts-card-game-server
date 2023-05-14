package edu.gmu.cs.hearts.service;

import edu.gmu.cs.hearts.domain.*;
import edu.gmu.cs.hearts.domain.card.Card;
import edu.gmu.cs.hearts.domain.card.Deck;
import edu.gmu.cs.hearts.domain.card.Rank;
import edu.gmu.cs.hearts.domain.card.Suit;
import edu.gmu.cs.hearts.model.GameInstance;
import edu.gmu.cs.hearts.model.PlayerInstance;
import edu.gmu.cs.hearts.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {

    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final PlayerRepository playerRepository;
    private final GameMoveRepository gameMoveRepository;
    private final HandWinnerRepository handWinnerRepository;

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
                        .score(0)
                        .build();
        GamePlayer joinedPlayer = gamePlayerRepository.save(gamePlayer);
        if(joinedPlayer.getDirection().equals(PlayerDirection.WEST)) {
            Game game = gameRepository.findById(gameId).get();
            game.setStatus(Status.READY);
            gameRepository.save(game);
        }
        return getGameInstance(playerEmail, gameId);
    }

    public GameInstance getGameInstance(String email, Integer gameId) {
        GameInstance gameInstance = new GameInstance();
        gameInstance.setCurrentMove(0);
        Game game = gameRepository.findById(gameId).get();
        gameInstance.setGameId(game.getId());
        gameInstance.setCurrentSuit(game.getCurrentSuit());
        List<GamePlayer> players = gamePlayerRepository.findGamePlayerByGameId(gameId);
        for(GamePlayer player: players) {
            Player playerData = playerRepository.findById(player.getPlayerId()).get();
            PlayerInstance playerInGame = PlayerInstance.builder()
                    .firstName(playerData.getFirstName())
                    .lastName(playerData.getLastName())
                    .playerId(playerData.getId())
                    .score(player.getScore())
                    .gameMoves(gameMoveRepository.getGameMovesByGameIdAndPlayerId(gameId, player.getPlayerId()))
                    .build();
            if(gameInstance.getCurrentMove() < playerInGame.getGameMoves().size()) {
                gameInstance.setCurrentMove(playerInGame.getGameMoves().size());
            }
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
        gameInstance.setHandWinners(handWinnerRepository.getHandWinnersByGameId(gameId));
        return gameInstance;
    }

    private boolean checkIfAllPlayerPassed(List<GamePlayer> players) {
        for(GamePlayer gamePlayer: players) {
            if(gamePlayer.getPassedCards() == null || gamePlayer.getPassedCards().equals("")) {
                return false;
            }
        }
        return true;
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

    public GameInstance playCard(String email, Integer gameId, String card) throws Exception {
        Game game = gameRepository.findById(gameId).get();
        Card playedCard = new Card(Suit.valueOf(card.split("-")[0]), Rank.valueOf(card.split("-")[1]));
        if(!game.getStatus().equals(Status.IN_PROGRESS)) {
            throw new Exception("Game is not in progress");
        }
        Player player = playerRepository.findByEmail(email).get();
        GamePlayer currentGamePlayer = gamePlayerRepository.findGamePlayerByGameIdAndDirection(gameId, game.getCurrentDirection());
        if(currentGamePlayer == null || !currentGamePlayer.getPlayerId().equals(player.getId())) {
            throw new Exception("Player Move Not Allowed");
        } else {
            String availableCards = currentGamePlayer.getCards();
            if(!availableCards.contains(card)) {
                throw new Exception("Invalid Card");
            } else {
                if(game.getCurrentDirection().equals(PlayerDirection.NORTH)) {
                    game.setCurrentSuit(playedCard.getSuit());
                    gameRepository.save(game);
                } else {
                    Suit currentSuit = game.getCurrentSuit();
                    if(!currentSuit.equals(playedCard.getSuit()) && availableCards.contains(currentSuit.toString())) {
                        throw new Exception("Card Move Not Allowed");
                    }
                }
                List<String> cards = List.of(availableCards.split(","));
                List<String> newCards = new ArrayList<>();
                for(String c: cards) {
                    if(!c.equals(card)){
                        newCards.add(c);
                    }
                }
                currentGamePlayer.setCards(String.join(",", newCards));
                gamePlayerRepository.save(currentGamePlayer);
                Integer moveCount = gameMoveRepository.countAllByGameIdAndPlayerId(game.getId(), currentGamePlayer.getPlayerId());
                GameMove gameMove = GameMove
                        .builder()
                        .moveNo(moveCount + 1)
                        .gameId(game.getId())
                        .playerId(currentGamePlayer.getPlayerId())
                        .cardPlayed(card)
                        .movePoints(0)
                        .build();
                gameMoveRepository.save(gameMove);
                if(game.getCurrentDirection().equals(PlayerDirection.NORTH)) {
                    game.setCurrentDirection(PlayerDirection.EAST);
                } else if(game.getCurrentDirection().equals(PlayerDirection.EAST)) {
                    game.setCurrentDirection(PlayerDirection.SOUTH);
                } else if(game.getCurrentDirection().equals(PlayerDirection.SOUTH)) {
                    game.setCurrentDirection(PlayerDirection.WEST);
                } else if(game.getCurrentDirection().equals(PlayerDirection.WEST)) {
                    game.setCurrentDirection(PlayerDirection.NORTH);
                    Integer moveNo = gameMoveRepository.maxMoveNumber(game.getId());
                    List<GameMove> gameMoves = gameMoveRepository.getGameMovesByGameIdAndMoveNo(game.getId(), moveNo);
                    Integer handWinner = getHandWinnerFromGameMoves(gameMoves, game.getCurrentSuit());
                    HandWinner handWinnerObject = HandWinner.builder()
                            .gameId(gameId)
                            .points(getPointsFromGameMoves(gameMoves))
                            .playerId(handWinner)
                            .moveNo(moveNo)
                            .build();
                    handWinnerRepository.save(handWinnerObject);
                    GamePlayer handWinnerPlayer = gamePlayerRepository.findGamePlayerByGameIdAndPlayerId(gameId, handWinner);
                    handWinnerPlayer.setScore(handWinnerPlayer.getScore() + getPointsFromGameMoves(gameMoves));
                }
                gameRepository.save(game);
                return getGameInstance(email, gameId);
            }
        }
    }

    public Integer getHandWinnerFromGameMoves(List<GameMove> gameMoves, Suit currentSuit) {
        List<GameMove> currentSuitCards = gameMoves.stream().filter(gameMove -> {
            return Suit.valueOf(gameMove.getCardPlayed().split("-")[0]).equals(currentSuit);
        }).toList();
        Integer winnerId = currentSuitCards.get(0).getPlayerId();
        int rank = getRank(currentSuitCards.get(0).getCardPlayed());
        for(GameMove move: currentSuitCards) {
            if(getRank(move.getCardPlayed()) > rank) {
                rank = getRank(move.getCardPlayed());
                winnerId = move.getPlayerId();
            }
        }
        return winnerId;
    }

    public Integer getPointsFromGameMoves(List<GameMove> gameMoves) {
        int totalPoints = 0;
        for(GameMove move: gameMoves) {
            Card card = new Card(Suit.valueOf(move.getCardPlayed().split("-")[0]), Rank.valueOf(move.getCardPlayed().split("-")[1]));
            if(card.getSuit().equals(Suit.HEARTS)) {
                totalPoints++;
            } else if(card.getSuit().equals(Suit.SPADES) && card.getRank().equals(Rank.QUEEN)) {
                totalPoints += 13;
            }
        }
        return totalPoints;
    }

    public int getRank(String card) {
        Rank rank = Rank.valueOf(card.split("-")[1]);
        return switch (rank) {
            case ACE -> 13;
            case KING -> 12;
            case QUEEN -> 11;
            case JACK -> 10;
            case TEN -> 9;
            case NINE -> 8;
            case EIGHT -> 7;
            case SEVEN -> 6;
            case SIX -> 5;
            case FIVE -> 4;
            case FOUR -> 3;
            case THREE -> 2;
            case TWO -> 1;
            default -> 0;
        };
    }

    public Boolean updateGarbageCards(String email, List<String> cards) throws Exception {
        Player player = playerRepository.findByEmail(email).get();
        GamePlayer gamePlayer = gamePlayerRepository.findGamePlayerByGameIdAndPlayerId(Integer.valueOf(cards.get(cards.size()-1)), player.getId());
        String gameCards = gamePlayer.getCards();
        for(int i=0; i<3; i++) {
            if(!gameCards.contains(cards.get(i))) {
                throw new Exception("Invalid Set of Cards for Garbage Pass");
            }
        }
        String garbageCards = String.join(",", cards.subList(0, 3));
        List<String> newCards = new ArrayList<>();
        for(String card: gameCards.split("-")) {
            if(!garbageCards.contains(card)) {
                newCards.add(card);
            }
        }
        gamePlayer.setCards(String.join(",", newCards));
        gamePlayer.setPassedCards(garbageCards);
        gamePlayerRepository.save(gamePlayer);
        return true;
    }
}
