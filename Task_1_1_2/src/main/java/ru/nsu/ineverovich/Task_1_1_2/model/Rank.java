package ru.nsu.ineverovich.Task_1_1_2.model;

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

    Rank(int baseValue, String cardName) {
        this.baseValue = baseValue;
        this.cardName = cardName;
    }

    public String getCardName() {
        return cardName;
    }

    public int getBaseValue() {
        return baseValue;
    }
}

