package ru.nsu.ineverovich.task112.model;

/**
 * Определяет масти карт.
 */
public enum Suit {
    SPADES("Пики"),
    HEARTS("Черви"),
    DIAMONDS("Бубны"),
    CLUBS("Трефы");

    private final String cardName;

    /**
     * Создаёт масть карты.
     *
     * @param cardName название масти
     */
    Suit(String cardName) {
        this.cardName = cardName;
    }

    /**
     * Возвращает название масти.
     *
     * @return название масти
     */
    public String getCardName() {
        return cardName;
    }
}
