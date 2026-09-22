package ru.nsu.ineverovich.Task_1_1_2.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Hand {
    public static final int CARD_COUNT = 2;
    public static final int SCORE = 21;
    public static final int ACE = 10;

    private final List<Card> cards = new ArrayList<>();

    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }
        cards.add(card);
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    public int getTotal() {
        int total = 0;
        int aces = 0;
        for (Card card : cards) {
            total += card.getValue();
            if (card.getRank() == Rank.ACE) {
                aces++;
            }
        }
        while (total > SCORE && aces > 0) {
            total -= ACE;
            aces--;
        }
        return total;
    }

    public boolean isBust() {
        return getTotal() > SCORE;
    }

    public boolean isBlackjack() {
        return cards.size() == CARD_COUNT && getTotal() == SCORE;
    }

    @Override
    public String toString() {
        return cards.toString();
    }
}
