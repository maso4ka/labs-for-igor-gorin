package ru.nsu.ineverovich.task112.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Хранит карты игрока и вычисляет их суммарное
 * значение.
 *
 */
public final class Hand {
    /** Количество карт в начальной руке Blackjack. */
    public static final int CARD_COUNT = 2;
    /** Максимальное значение руки без перебора. */
    public static final int SCORE = 21;
    /** Разница между значениями туза 11 и 1. */
    public static final int ACE = 10;

    private final List<Card> cards = new ArrayList<>();

    /**
     * Добавляет карту в руку.
     *
     * @param card добавляемая карта
     * @throws IllegalArgumentException если карта равна {@code null}
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }
        cards.add(card);
    }

    /**
     * Возвращает карты руки в неизменяемом списке.
     *
     * @return неизменяемый список карт
     */
    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

    /**
     * Вычисляет итоговое значение руки с учётом тузов.
     *
     * @return итоговое значение руки
     */
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

    /**
     * Проверяет, превышает ли значение руки 21.
     *
     * @return {@code true}, если рука перебрала 21
     */
    public boolean isBust() {
        return getTotal() > SCORE;
    }

    /**
     * Проверяет, является ли рука комбинацией Blackjack.
     *
     * @return {@code true}, если рука содержит две карты с суммой 21
     */
    public boolean isBlackjack() {
        return cards.size() == CARD_COUNT && getTotal() == SCORE;
    }

    /**
     * Возвращает текстовое представление руки.
     *
     * @return список карт руки
     */
    @Override
    public String toString() {
        return cards.toString();
    }
}
