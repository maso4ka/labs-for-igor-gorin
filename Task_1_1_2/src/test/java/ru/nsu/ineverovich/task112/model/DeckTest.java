package ru.nsu.ineverovich.task112.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

class DeckTest {
    private static final int ONE_DECK_SIZE = 52;
    private static final int TWO_DECK_SIZE = 104;
    private static final int ONE_CARD = 1;

    @Test
    void createsStandardDeck() {
        Deck deck = new Deck(Deck.STANDARD_DECK_COUNT);

        assertEquals(ONE_DECK_SIZE, deck.size());
        assertTrue(deck.getClass().getSimpleName().equals("Deck"));
    }

    @Test
    void createsMultipleDecks() {
        Deck deck = new Deck(2);

        assertEquals(TWO_DECK_SIZE, deck.size());
    }

    @Test
    void drawsCardsAndDecreasesSize() {
        Deck deck = new Deck(List.of(
                new Card(Suit.SPADES, Rank.ACE),
                new Card(Suit.HEARTS, Rank.KING)));

        Card first = deck.draw();
        assertEquals(Rank.ACE, first.getRank());
        assertEquals(ONE_CARD, deck.size());
        assertTrue(!deck.isEmpty());

        deck.draw();
        assertTrue(deck.isEmpty());
    }

    @Test
    void rejectsInvalidDeckCount() {
        assertThrows(IllegalArgumentException.class, () -> new Deck(0));
    }

    @Test
    void rejectsEmptyCustomDeck() {
        assertThrows(IllegalArgumentException.class, () -> new Deck(List.of()));
        assertThrows(IllegalArgumentException.class, () -> new Deck(null));
    }

    @Test
    void rejectsDrawingFromEmptyDeck() {
        Deck deck = new Deck(List.of(new Card(Suit.SPADES, Rank.ACE)));
        deck.draw();

        assertThrows(IllegalStateException.class, deck::draw);
    }
}