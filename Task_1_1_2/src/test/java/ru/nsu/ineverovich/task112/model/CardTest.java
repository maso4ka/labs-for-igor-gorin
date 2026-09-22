package ru.nsu.ineverovich.task112.model;


import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class CardTest {
    private static final String EXPECTED_NAME = "Дама Пики";
    private static final int EXPECTED_VALUE = 10;

    @Test
    void exposesCardData() {
        Card card = new Card(Suit.SPADES, Rank.QUEEN);

        assertEquals(Suit.SPADES, card.getSuit());
        assertEquals(Rank.QUEEN, card.getRank());
        assertEquals(EXPECTED_VALUE, card.getValue());
        assertEquals(EXPECTED_NAME, card.toString());
    }
}