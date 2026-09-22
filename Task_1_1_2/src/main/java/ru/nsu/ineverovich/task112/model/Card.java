package ru.nsu.ineverovich.task112.model;

/**
 * Представляет игровую карту с мастью и достоинством.
 */
public final class Card {
    private final Suit suit;
    private final Rank rank;

    /**
     * Создаёт карту.
     *
     * @param suit масть карты
     * @param rank достоинство карты
     */
    public Card(Suit suit, Rank rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Возвращает масть карты.
     *
     * @return масть карты
     */
    public Suit getSuit() {
        return suit;
    }

    /**
     * Возвращает достоинство карты.
     *
     * @return достоинство карты
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Возвращает числовое значение карты.
     *
     * @return числовое значение карты
     */
    public int getValue() {
        return rank.getBaseValue();
    }

    /**
     * Возвращает текстовое представление карты.
     *
     * @return название карты и её масть
     */
    @Override
    public String toString() {
        return rank.getCardName() + " " + suit.getCardName();
    }
}
