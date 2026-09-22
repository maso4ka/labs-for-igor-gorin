package ru.nsu.ineverovich.task112.game;

import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет состоянием одного раунда Blackjack.
 */
public final class Round {
    /**
     * Определяет возможный результат раунда.
     */
    public enum Result {
        PLAYER_WIN,
        DEALER_WIN,
        DRAW
    }

    private static final int INITIAL_CARD_COUNT = 2;

    private final Deck deck;
    private final User user;
    private final Dealer dealer;
    private Card hiddenDealerCard;
    private boolean finished;

    /**
     * Создаёт новый раунд.
     *
     * @param deck колода для раунда
     * @param playerName имя игрока
     */
    public Round(Deck deck, String playerName) {
        if (deck == null) {
            throw new IllegalArgumentException("Колода не может быть null");
        }
        this.deck = deck;
        user = new User(playerName);
        dealer = new Dealer();
    }

    /**
     * Раздаёт игроку и дилеру начальные карты.
     */
    public void dealInitialCards() {
        for (int cardIndex = 0; cardIndex < INITIAL_CARD_COUNT; cardIndex++) {
            user.receiveCard(deck.draw());
            if (cardIndex == INITIAL_CARD_COUNT - 1) {
                hiddenDealerCard = deck.draw();
            } else {
                dealer.receiveCard(deck.draw());
            }
        }
    }

    /**
     * Берёт следующую карту из колоды.
     *
     * @return следующая карта
     */
    public Card drawCard() {
        return deck.draw();
    }

    /**
     * Возвращает игрока текущего раунда.
     *
     * @return игрок
     */
    public User getUser() {
        return user;
    }

    /**
     * Возвращает дилера текущего раунда.
     *
     * @return дилер
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Возвращает закрытую карту дилера.
     *
     * @return закрытая карта или {@code null}, если карта открыта
     */
    public Card getHiddenDealerCard() {
        return hiddenDealerCard;
    }

    /**
     * Открывает закрытую карту дилера.
     */
    public void revealDealerCard() {
        if (hiddenDealerCard != null) {
            dealer.receiveCard(hiddenDealerCard);
            hiddenDealerCard = null;
        }
    }

    /**
     * Проверяет, остаётся ли карта дилера закрытой.
     *
     * @return {@code true}, если карта закрыта
     */
    public boolean isDealerCardHidden() {
        return hiddenDealerCard != null;
    }

    /**
     * Проверяет, завершён ли раунд.
     *
     * @return {@code true}, если раунд завершён
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Помечает раунд как завершённый.
     */
    public void finish() {
        finished = true;
    }

    /**
     * Определяет результат раунда по картам игрока
 * и дилера.
     *
     * @return результат раунда
     */
    public Result determineResult() {
        if (user.hasBlackjack() && dealer.hasBlackjack()) {
            return Result.DRAW;
        }
        if (user.hasBlackjack() || dealer.isBust()) {
            return Result.PLAYER_WIN;
        }
        if (dealer.hasBlackjack() || user.isBust()) {
            return Result.DEALER_WIN;
        }
        return compareScores();
    }

    private Result compareScores() {
        if (user.getScore() > dealer.getScore()) {
            return Result.PLAYER_WIN;
        }
        if (user.getScore() < dealer.getScore()) {
            return Result.DEALER_WIN;
        }
        return Result.DRAW;
    }
}
