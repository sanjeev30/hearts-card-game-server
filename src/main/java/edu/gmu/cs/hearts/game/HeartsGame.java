package edu.gmu.cs.hearts.game;

import java.util.List;

public class HeartsGame {

    private Long gameId;
    private int playersCount;
    private List<GamePlayer> players;

    public Long getId() {
        return this.gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public int getPlayersCount() {
        return this.playersCount;
    }

    public void setPlayersCount(int playersCount) {
        this.playersCount = playersCount;
    }
}
