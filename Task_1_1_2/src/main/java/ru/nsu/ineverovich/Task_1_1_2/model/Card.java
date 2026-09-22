package ru.nsu.ineverovich.Task_1_1_2.model;

public final class Card {
    private final Suit suit;
    private final Rank rank;

    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    public Suit getSuit() {
        return suit;
    }

    public Rank getRank() {
        return rank;
    }

    public int getValue() {
        return rank.getBaseValue();
    }

    @Override
    public String toString() {
        return rank.getCardName() + " " + suit.getCardName();
    }
}
