package edu.gmu.cs.hearts.game;

import edu.gmu.cs.hearts.domain.card.Card;

import java.util.ArrayList;
import java.util.List;

public class GamePlayer {
    private final String name;
    private List<Card> hand;

    public GamePlayer(String name) {
        this.name = name;
        hand = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Card> getHand() {
        return hand;
    }

    public void addCardToHand(Card card) {
        hand.add(card);
    }

    public void removeCardFromHand(Card card) {
        hand.remove(card);
    }
}
