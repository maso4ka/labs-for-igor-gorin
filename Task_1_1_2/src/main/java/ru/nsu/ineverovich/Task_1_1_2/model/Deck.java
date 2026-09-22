package ru.nsu.ineverovich.Task_1_1_2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Deck {
    public static final int CARDS_PER_DECK = 52;
    private static final int MIN_DECK_COUNT = 1;

    private final List<Card> cards;
    private int position;

    public Deck(int deckCount) {
        if (deckCount < MIN_DECK_COUNT) {
            throw new IllegalArgumentException("Количество колод должно быть не меньше 1");
        }
        cards = createCards(deckCount);
        shuffle();
    }

    public Deck(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException("Колода не может быть пустой");
        }
        this.cards = new ArrayList<>(cards);
        position = 0;
    }

    private static List<Card> createCards(int deckCount) {
        List<Card> result = new ArrayList<>(deckCount * CARDS_PER_DECK);
        for (int deckIndex = 0; deckIndex < deckCount; deckIndex++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    result.add(new Card(suit, rank));
                }
            }
        }
        return result;
    }

    public void shuffle() {
        Collections.shuffle(cards);
        position = 0;
    }

    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("В колоде закончились карты");
        }
        Card card = cards.get(position);
        position++;
        return card;
    }

    public boolean isEmpty() {
        return position >= cards.size();
    }

    public int size() {
        return cards.size() - position;
    }
}

