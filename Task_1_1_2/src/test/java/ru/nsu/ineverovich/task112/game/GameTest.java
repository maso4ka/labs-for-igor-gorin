package ru.nsu.ineverovich.task112.game;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.User;

class GameTest {
    private static final String PLAYER_NAME = "Иван";

    @Test
    void keepsSamePlayersBetweenRounds() {
        Game game = new Game(PLAYER_NAME);

        User user = game.getUser();
        Dealer dealer = game.getDealer();

        Round first = game.startRound();

        assertEquals(1, first.getRoundNumber());
        assertEquals(2, user.getHand().getCards().size());

        Round second = game.startRound();

        assertEquals(user, game.getUser());
        assertEquals(dealer, game.getDealer());
        assertEquals(2, second.getRoundNumber());
        assertEquals(2, game.getUser().getHand().getCards().size());
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
}