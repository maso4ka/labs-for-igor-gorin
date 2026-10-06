package ru.nsu.ineverovich.task112.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Хранит карты игрока и вычисляет их суммарное
 * значение.
 */
public final class Hand {
    private static final int MAX_SCORE = 21;
    private static final int ACE_DIFFERENCE = 10;

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

        while (total > MAX_SCORE && aces > 0) {
            total -= ACE_DIFFERENCE;
            aces--;
        }

        return total;
    }

    /**
     * Очищает руку перед новым раундом.
     */
    public void clear() {
        cards.clear();
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