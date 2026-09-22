package ru.nsu.ineverovich.task112.model;

/**
 * Определяет достоинства карт и их базовые значения.
 */
public enum Rank {
    TWO(2, "Двойка"),
    THREE(3, "Тройка"),
    FOUR(4, "Четверка"),
    FIVE(5, "Пятерка"),
    SIX(6, "Шестерка"),
    SEVEN(7, "Семерка"),
    EIGHT(8, "Восьмерка"),
    NINE(9, "Девятка"),
    TEN(10, "Десятка"),
    JACK(10, "Валет"),
    QUEEN(10, "Дама"),
    KING(10, "Король"),
    ACE(11, "Туз");

    private final String cardName;
    private final int baseValue;

    /**
     * Создаёт достоинство карты.
     *
     * @param baseValue базовое значение карты
     * @param cardName название карты
     */
    Rank(int baseValue, String cardName) {
        this.baseValue = baseValue;
        this.cardName = cardName;
    }

    /**
     * Возвращает название достоинства карты.
     *
     * @return название карты
     */
    public String getCardName() {
        return cardName;
    }

    /**
     * Возвращает базовое числовое значение карты.
     *
     * @return базовое значение карты
     */
    public int getBaseValue() {
        return baseValue;
    }
}
