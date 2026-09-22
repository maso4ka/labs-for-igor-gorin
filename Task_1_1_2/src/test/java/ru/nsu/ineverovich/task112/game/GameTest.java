package ru.nsu.ineverovich.task112.game;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.Rank;
import ru.nsu.ineverovich.task112.model.Suit;

class GameTest {
    private static final String PLAYER_NAME = "Иван";
    private static final int FIRST_ROUND = 1;
    private static final int SECOND_ROUND = 2;

    @Test
    void createsRounds() {
        Game game = new Game(1, PLAYER_NAME);
        Deck firstDeck = deck();
        Deck secondDeck = deck();

        Round first = game.createRound(firstDeck);
        assertEquals(FIRST_ROUND, game.getRoundNumber());
        Round second = game.createRound(secondDeck);

        assertEquals(2, first.getUser().getHand().getCards().size());
        assertEquals(SECOND_ROUND, game.getRoundNumber());
        assertEquals(2, second.getUser().getHand().getCards().size());
    }

    @Test
    void registersWins() {
        Game game = new Game(1, PLAYER_NAME);

        game.registerResult(Round.Result.PLAYER_WIN);
        game.registerResult(Round.Result.DEALER_WIN);
        game.registerResult(Round.Result.DRAW);

        assertEquals(1, game.getPlayerWins());
        assertEquals(1, game.getDealerWins());
        assertEquals("Счет 1:1 в вашу пользу.", game.getScoreText());
    }

    private static Deck deck() {
        return new Deck(List.of(
                new Card(Suit.SPADES, Rank.TEN),
                new Card(Suit.HEARTS, Rank.SIX),
                new Card(Suit.DIAMONDS, Rank.NINE),
                new Card(Suit.CLUBS, Rank.ACE)));
    }
}