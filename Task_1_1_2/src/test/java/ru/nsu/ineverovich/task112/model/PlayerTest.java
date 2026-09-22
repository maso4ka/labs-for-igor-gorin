package ru.nsu.ineverovich.task112.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PlayerTest {
    private static final String PLAYER_NAME = "Иван";
    private static final int BLACKJACK = 21;

    @Test
    void userReceivesCardsAndReportsState() {
        User user = new User(PLAYER_NAME);
        user.receiveCard(new Card(Suit.SPADES, Rank.ACE));
        user.receiveCard(new Card(Suit.HEARTS, Rank.KING));

        assertEquals(PLAYER_NAME, user.getName());
        assertEquals(BLACKJACK, user.getScore());
        assertTrue(user.hasBlackjack());
        assertFalse(user.isBust());
    }

    @Test
    void dealerName() {
        Dealer dealer = new Dealer();

        assertEquals("Дилер", dealer.getName());
    }
}