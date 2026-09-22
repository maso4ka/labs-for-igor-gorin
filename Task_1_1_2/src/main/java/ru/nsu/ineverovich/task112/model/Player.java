package ru.nsu.ineverovich.task112.model;

/**
 * Представляет участника игры Blackjack и его руку.
 */
public abstract class Player {
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
     * Возвращает текущее значение руки участника.
     *
     * @return значение руки
     */
    public int getScore() {
        return hand.getTotal();
    }

    /**
     * Проверяет, перебрал ли участник 21.
     *
     * @return {@code true}, если участник перебрал 21
     */
    public boolean isBust() {
        return hand.isBust();
    }

    /**
     * Проверяет, есть ли у участника Blackjack.
     *
     * @return {@code true}, если у участника Blackjack
     */
    public boolean hasBlackjack() {
        return hand.isBlackjack();
    }
}
