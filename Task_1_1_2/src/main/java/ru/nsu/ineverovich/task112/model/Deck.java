package ru.nsu.ineverovich.task112.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Представляет колоду карт Blackjack.
 */
public final class Deck {
    private static final int MIN_DECK_COUNT = 1;

    private final List<Card> cards;
    private int position;

    /**
     * Создаёт и перемешивает указанное количество
     * стандартных колод.
     *
     * @param deckCount количество колод
     */
    public Deck(int deckCount) {
        if (deckCount < MIN_DECK_COUNT) {
            throw new IllegalArgumentException(
                    "Количество колод должно быть не меньше 1");
        }
        cards = createCards(deckCount);
        shuffle();
    }

    /**
     * Создаёт колоду из переданного списка карт.
     *
     * @param cards карты колоды
     */
    public Deck(List<Card> cards) {
        if (cards == null || cards.isEmpty()) {
            throw new IllegalArgumentException(
                    "Колода не может быть пустой");
        }
        this.cards = new ArrayList<>(cards);
        position = 0;
    }

    private static List<Card> createCards(int deckCount) {
        List<Card> result = new ArrayList<>(
                deckCount * DeckConstants.CARDS_PER_DECK);

        for (int deckIndex = 0;
             deckIndex < deckCount;
             deckIndex++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    result.add(new Card(suit, rank));
                }
            }
        }

        return result;
    }

    /**
     * Перемешивает карты и начинает раздачу
     * с первой карты.
     */
    public void shuffle() {
        Collections.shuffle(cards);
        position = 0;
    }

    /**
     * Извлекает следующую карту из колоды.
     *
     * @return извлечённая карта
     * @throws IllegalStateException если в колоде не осталось карт
     */
    public Card draw() {
        if (isEmpty()) {
            throw new IllegalStateException("В колоде закончились карты");
        }

        Card card = cards.get(position);
        position++;
        return card;
    }

    /**
     * Проверяет, закончились ли карты в колоде.
     *
     * @return {@code true}, если карт не осталось
     */
    public boolean isEmpty() {
        return position >= cards.size();
    }

    /**
     * Возвращает количество оставшихся карт.
     *
     * @return количество оставшихся карт
     */
    public int size() {
        return cards.size() - position;
    }
}