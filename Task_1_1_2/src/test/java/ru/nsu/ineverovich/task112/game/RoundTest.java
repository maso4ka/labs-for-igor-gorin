package ru.nsu.ineverovich.task112.game;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.Rank;
import ru.nsu.ineverovich.task112.model.Suit;
import ru.nsu.ineverovich.task112.model.User;

class RoundTest {
    private static final String PLAYER_NAME = "Иван";

    @Test
    void rejectsNullDeck() {
        Game game = new Game(PLAYER_NAME);
        assertThrows(
                IllegalArgumentException.class,
                () -> new Round(null, game.getUser(), game.getDealer(), 1));
    }

    @Test
    void rejectsNullParticipants() {
        Game game = new Game(PLAYER_NAME);
        Deck deck = deck(card(Rank.ACE));

        assertThrows(
                IllegalArgumentException.class,
                () -> new Round(deck, null, game.getDealer(), 1));
        assertThrows(
                IllegalArgumentException.class,
                () -> new Round(deck, game.getUser(), null, 1));
    }

    @Test
    void dealsInitialCardsAndHidesDealerCard() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE), card(Rank.TEN), card(Rank.KING), card(Rank.SIX)),
                game.getUser(),
                game.getDealer(),
                3);

        round.dealInitialCards();

        assertEquals(3, round.getRoundNumber());
        assertEquals(2, round.getUser().getHand().getCards().size());
        assertEquals(1, round.getDealer().getHand().getCards().size());
        assertTrue(round.isDealerCardHidden());
    }

    @Test
    void drawsCardFromDeck() {
        Game game = new Game(PLAYER_NAME);
        Card expected = card(Rank.ACE);
        Round round = new Round(
                deck(expected), game.getUser(), game.getDealer(), 1);

        assertEquals(expected, round.drawCard());
    }

    @Test
    void finishesRound() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);

        assertFalse(round.isFinished());
        round.finish();
        assertTrue(round.isFinished());
    }

    @Test
    void playerAndDealerBlackjackResultIsDraw() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.ACE));
        game.getUser().receiveCard(card(Rank.KING));
        game.getDealer().receiveCard(card(Rank.ACE));
        game.getDealer().receiveCard(card(Rank.QUEEN));

        assertEquals(Result.DRAW, round.determineResult());
    }

    @Test
    void playerBlackjackWins() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.ACE));
        game.getUser().receiveCard(card(Rank.KING));
        game.getDealer().receiveCard(card(Rank.TEN));
        game.getDealer().receiveCard(card(Rank.SIX));

        assertEquals(Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void dealerBustGivesPlayerWin() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.TEN));
        game.getUser().receiveCard(card(Rank.SIX));
        game.getDealer().receiveCard(card(Rank.KING));
        game.getDealer().receiveCard(card(Rank.QUEEN));
        game.getDealer().receiveCard(card(Rank.THREE));

        assertEquals(Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void dealerBlackjackWins() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.TEN));
        game.getUser().receiveCard(card(Rank.NINE));
        game.getDealer().receiveCard(card(Rank.ACE));
        game.getDealer().receiveCard(card(Rank.KING));

        assertEquals(Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void playerBustGivesDealerWin() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.KING));
        game.getUser().receiveCard(card(Rank.QUEEN));
        game.getUser().receiveCard(card(Rank.THREE));
        game.getDealer().receiveCard(card(Rank.TEN));
        game.getDealer().receiveCard(card(Rank.SIX));

        assertEquals(Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void higherPlayerScoreWins() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.TEN));
        game.getUser().receiveCard(card(Rank.EIGHT));
        game.getDealer().receiveCard(card(Rank.TEN));
        game.getDealer().receiveCard(card(Rank.SEVEN));

        assertEquals(Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void higherDealerScoreWins() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.TEN));
        game.getUser().receiveCard(card(Rank.TWO));
        game.getDealer().receiveCard(card(Rank.TEN));
        game.getDealer().receiveCard(card(Rank.NINE));

        assertEquals(Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void equalScoresAreDraw() {
        Game game = new Game(PLAYER_NAME);
        Round round = new Round(
                deck(card(Rank.ACE)), game.getUser(), game.getDealer(), 1);
        game.getUser().receiveCard(card(Rank.TEN));
        game.getUser().receiveCard(card(Rank.EIGHT));
        game.getDealer().receiveCard(card(Rank.NINE));
        game.getDealer().receiveCard(card(Rank.NINE));

        assertEquals(Result.DRAW, round.determineResult());
    }

    private static Deck deck(Card... cards) {
        return new Deck(List.of(cards));
    }

    private static Card card(Rank rank) {
        return new Card(Suit.SPADES, rank);
    }
}
