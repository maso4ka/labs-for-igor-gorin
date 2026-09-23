package ru.nsu.ineverovich.task112.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.Rank;
import ru.nsu.ineverovich.task112.model.Suit;
import ru.nsu.ineverovich.task112.model.User;

class GameTest {
    private static final String PLAYER_NAME = "Иван";

    @Test
    void keepsSamePlayersBetweenRounds() {
        Game game = new Game(PLAYER_NAME);
        User user = game.getUser();
        Dealer dealer = game.getDealer();

        Round first = new Round(deck(), user, dealer, 1);
        first.dealInitialCards();
        Round second = new Round(deck(), user, dealer, 2);
        second.dealInitialCards();

        assertEquals(user, second.getUser());
        assertEquals(dealer, second.getDealer());
        assertEquals(2, second.getRoundNumber());
        assertEquals(2, second.getUser().getHand().getCards().size());
    }

    @Test
    void registersWins() {
        Game game = new Game(PLAYER_NAME);

        game.registerResult(Result.PLAYER_WIN);
        game.registerResult(Result.DEALER_WIN);
        game.registerResult(Result.DRAW);

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
