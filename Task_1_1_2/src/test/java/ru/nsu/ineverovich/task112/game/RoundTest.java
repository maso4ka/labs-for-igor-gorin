package ru.nsu.ineverovich.task112.game;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.Rank;
import ru.nsu.ineverovich.task112.model.Suit;

class RoundTest {
    private static final String PLAYER_NAME = "Иван";

    @Test
    void rejectsNullDeck() {
        assertThrows(IllegalArgumentException.class, () -> new Round(null, PLAYER_NAME));
    }

    @Test
    void dealsInitialCardsAndHidesDealerCard() {
        Round round = new Round(deck(
                card(Rank.ACE),
                card(Rank.TEN),
                card(Rank.KING),
                card(Rank.SIX)), PLAYER_NAME);

        round.dealInitialCards();

        assertEquals(2, round.getUser().getHand().getCards().size());
        assertEquals(1, round.getDealer().getHand().getCards().size());
        assertTrue(round.isDealerCardHidden());
        assertEquals(Rank.SIX, round.getHiddenDealerCard().getRank());
    }

    @Test
    void drawsCardFromDeck() {
        Card expected = card(Rank.ACE);
        Round round = new Round(deck(expected), PLAYER_NAME);

        assertEquals(expected, round.drawCard());
    }

    @Test
    void revealsDealerCard() {
        Round round = new Round(deck(
                card(Rank.ACE),
                card(Rank.TEN),
                card(Rank.KING),
                card(Rank.SIX)), PLAYER_NAME);
        round.dealInitialCards();

        round.revealDealerCard();

        assertFalse(round.isDealerCardHidden());
        assertNull(round.getHiddenDealerCard());
        assertEquals(2, round.getDealer().getHand().getCards().size());
    }

    @Test
    void revealingAlreadyOpenedCardDoesNothing() {
        Round round = new Round(deck(
                card(Rank.ACE),
                card(Rank.TEN),
                card(Rank.KING),
                card(Rank.SIX)), PLAYER_NAME);
        round.dealInitialCards();
        round.revealDealerCard();

        round.revealDealerCard();

        assertEquals(2, round.getDealer().getHand().getCards().size());
    }

    @Test
    void finishesRound() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);

        assertFalse(round.isFinished());

        round.finish();

        assertTrue(round.isFinished());
    }

    @Test
    void playerAndDealerBlackjackResultIsDraw() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.ACE));
        round.getUser().receiveCard(card(Rank.KING));
        round.getDealer().receiveCard(card(Rank.ACE));
        round.getDealer().receiveCard(card(Rank.QUEEN));

        assertEquals(Round.Result.DRAW, round.determineResult());
    }

    @Test
    void playerBlackjackWins() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.ACE));
        round.getUser().receiveCard(card(Rank.KING));
        round.getDealer().receiveCard(card(Rank.TEN));
        round.getDealer().receiveCard(card(Rank.SIX));

        assertEquals(Round.Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void dealerBustGivesPlayerWin() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.TEN));
        round.getUser().receiveCard(card(Rank.SIX));
        round.getDealer().receiveCard(card(Rank.KING));
        round.getDealer().receiveCard(card(Rank.QUEEN));
        round.getDealer().receiveCard(card(Rank.THREE));

        assertEquals(Round.Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void dealerBlackjackWins() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.TEN));
        round.getUser().receiveCard(card(Rank.NINE));
        round.getDealer().receiveCard(card(Rank.ACE));
        round.getDealer().receiveCard(card(Rank.KING));

        assertEquals(Round.Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void playerBustGivesDealerWin() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.KING));
        round.getUser().receiveCard(card(Rank.QUEEN));
        round.getUser().receiveCard(card(Rank.THREE));
        round.getDealer().receiveCard(card(Rank.TEN));
        round.getDealer().receiveCard(card(Rank.SIX));

        assertEquals(Round.Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void higherPlayerScoreWins() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.TEN));
        round.getUser().receiveCard(card(Rank.EIGHT));
        round.getDealer().receiveCard(card(Rank.TEN));
        round.getDealer().receiveCard(card(Rank.SEVEN));

        assertEquals(Round.Result.PLAYER_WIN, round.determineResult());
    }

    @Test
    void higherDealerScoreWins() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.TEN));
        round.getUser().receiveCard(card(Rank.TWO));
        round.getDealer().receiveCard(card(Rank.TEN));
        round.getDealer().receiveCard(card(Rank.NINE));

        assertEquals(Round.Result.DEALER_WIN, round.determineResult());
    }

    @Test
    void equalScoresAreDraw() {
        Round round = new Round(deck(card(Rank.ACE)), PLAYER_NAME);
        round.getUser().receiveCard(card(Rank.TEN));
        round.getUser().receiveCard(card(Rank.EIGHT));
        round.getDealer().receiveCard(card(Rank.NINE));
        round.getDealer().receiveCard(card(Rank.NINE));

        assertEquals(Round.Result.DRAW, round.determineResult());
    }

    private static Deck deck(Card... cards) {
        return new Deck(List.of(cards));
    }

    private static Card card(Rank rank) {
        return new Card(Suit.SPADES, rank);
    }
}