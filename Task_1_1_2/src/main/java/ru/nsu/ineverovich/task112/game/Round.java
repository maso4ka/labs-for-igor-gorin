package ru.nsu.ineverovich.task112.game;

import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет состоянием одного раунда Blackjack.
 */
public final class Round {
    private static final int INITIAL_CARD_COUNT = 2;

    private final int roundNumber;
    private final Deck deck;
    private final User user;
    private final Dealer dealer;
    private boolean finished;

    /**
     * Создаёт новый раунд.
     *
     * @param deck колода для раунда
     * @param user игрок игры
     * @param dealer дилер игры
     * @param roundNumber номер раунда
     */
    public Round(Deck deck, User user, Dealer dealer, int roundNumber) {
        if (deck == null) {
            throw new IllegalArgumentException("Колода не может быть null");
        }
        if (user == null || dealer == null) {
            throw new IllegalArgumentException("Участники не могут быть null");
        }
        this.deck = deck;
        this.user = user;
        this.dealer = dealer;
        this.roundNumber = roundNumber;
        user.resetHand();
        dealer.resetHand();
    }

    /**
     * Раздаёт игроку и дилеру начальные карты.
     */
    public void dealInitialCards() {
        for (int cardIndex = 0; cardIndex < INITIAL_CARD_COUNT; cardIndex++) {
            user.receiveCard(deck.draw());
            if (cardIndex == INITIAL_CARD_COUNT - 1) {
                dealer.receiveHiddenCard(deck.draw());
            } else {
                dealer.receiveCard(deck.draw());
            }
        }
    }

    /**
     * Возвращает номер раунда.
     *
     * @return номер раунда
     */
    public int getRoundNumber() {
        return roundNumber;
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
     * Проверяет, остаётся ли карта дилера закрытой.
     *
     * @return {@code true}, если карта закрыта
     */
    public boolean isDealerCardHidden() {
        return dealer.isHiddenCardPresent();
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
