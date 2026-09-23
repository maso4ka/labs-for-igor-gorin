package ru.nsu.ineverovich.task112.model;

/**
 * Представляет дилера в игре Blackjack.
 */
public final class Dealer extends Player {
    /**
     * Минимальное значение руки, при котором
     * дилер останавливается.
     */
    public static final int STAND_SCORE = 17;

    private Card hiddenCard;

    /**
     * Создаёт дилера.
     */
    public Dealer() {
        super("Дилер");
    }

    /**
     * Принимает закрытую карту.
     *
     * @param card закрытая карта
     */
    public void receiveHiddenCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Карта не может быть null");
        }
        hiddenCard = card;
    }

    /**
     * Открывает закрытую карту дилера.
     */
    public void revealHiddenCard() {
        if (hiddenCard != null) {
            receiveCard(hiddenCard);
            hiddenCard = null;
        }
    }

    /**
     * Проверяет, образует ли закрытая карта Blackjack
     * с открытой картой.
     *
     * @return {@code true}, если у дилера Blackjack с учётом
     *         закрытой карты
     */
    public boolean hasHiddenBlackjack() {
        if (hiddenCard == null) {
            return hasBlackjack();
        }
        Hand possibleHand = new Hand();
        for (Card card : getHand().getCards()) {
            possibleHand.addCard(card);
        }
        possibleHand.addCard(hiddenCard);
        return possibleHand.getCards().size() == Hand.CARD_COUNT
                && possibleHand.getTotal() == Hand.SCORE;
    }

    /**
     * Проверяет наличие закрытой карты.
     *
     * @return {@code true}, если карта закрыта
     */
    public boolean isHiddenCardPresent() {
        return hiddenCard != null;
    }

    /**
     * Сбрасывает состояние руки и закрытой карты.
     */
    @Override
    public void resetHand() {
        super.resetHand();
        hiddenCard = null;
    }
}
