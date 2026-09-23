package ru.nsu.ineverovich.task112.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class HandTest {
    private static final int SCORE_FOURTEEN = 14;
    private static final int SCORE_TWENTY = 20;
    private static final int SCORE_TWENTY_ONE = 21;
    private static final int SCORE_TWENTY_THREE = 23;

    @Test
    void calculatesNumberCards() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.TEN));
        hand.addCard(card(Rank.SEVEN));
        hand.addCard(card(Rank.THREE));

        assertEquals(SCORE_TWENTY, hand.getTotal());
    }

    @Test
    void changesAce() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.SIX));
        hand.addCard(card(Rank.SEVEN));

        assertEquals(SCORE_FOURTEEN, hand.getTotal());
    }

    @Test
    void keepsAce() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.NINE));

        assertEquals(SCORE_TWENTY, hand.getTotal());
    }

    @Test
    void canHaveMultipleAces() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.ACE));
        hand.addCard(card(Rank.NINE));

        assertEquals(SCORE_TWENTY_ONE, hand.getTotal());
    }

    @Test
    void calculatesBustScore() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.KING));
        hand.addCard(card(Rank.QUEEN));
        hand.addCard(card(Rank.THREE));

        assertEquals(SCORE_TWENTY_THREE, hand.getTotal());
    }

    @Test
    void rejectsNullCard() {
        Hand hand = new Hand();

        assertThrows(IllegalArgumentException.class, () -> hand.addCard(null));
    }

    @Test
    void returnsUnmodifiableCards() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.TEN));

        assertThrows(
                UnsupportedOperationException.class,
                () -> hand.getCards().add(card(Rank.TWO)));
    }

    @Test
    void clearsCards() {
        Hand hand = new Hand();
        hand.addCard(card(Rank.TEN));

        hand.clear();

        assertEquals(0, hand.getCards().size());
    }

    private static Card card(Rank rank) {
        return new Card(Suit.SPADES, rank);
    }
}
