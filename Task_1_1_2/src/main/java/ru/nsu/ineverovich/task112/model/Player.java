package ru.nsu.ineverovich.task112.model;

/**
 * Представляет участника игры Blackjack и его руку.
 */
public abstract class Player {
    private static final int BLACKJACK_SCORE = 21;
    private static final int BLACKJACK_CARD_COUNT = 2;

    private final String name;
    private final Hand hand = new Hand();

    /**
     * Создаёт участника игры.
     *
     * @param name имя участника
     */
    protected Player(String name) {
        this.name = name;
    }

    /**
     * Возвращает имя участника.
     *
     * @return имя участника
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает руку участника.
     *
     * @return рука участника
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * Добавляет карту в руку участника.
     *
     * @param card добавляемая карта
     */
    public void receiveCard(Card card) {
        hand.addCard(card);
    }

    /**
     * Очищает руку участника перед новым раундом.
     */
    public void resetHand() {
        hand.clear();
    }

    /**
     * Возвращает текущее значение руки участника.
     *
     * @return значение руки
     */
    public int getScore() {
        return hand.getTotal();
    }

    /**
     * Проверяет состояние руки и определяет перебор.
     *
     * @return {@code true}, если участник набрал больше 21
     */
    public boolean isBust() {
        return getScore() > BLACKJACK_SCORE;
    }

    /**
     * Проверяет состояние руки и определяет Blackjack.
     *
     * @return {@code true}, если у участника Blackjack
     */
    public boolean hasBlackjack() {
        return hand.getCards().size() == BLACKJACK_CARD_COUNT
                && getScore() == BLACKJACK_SCORE;
    }
}