package ru.nsu.ineverovich.task112.game;

import ru.nsu.ineverovich.task112.model.Deck;

/**
 * Управляет игровыми раундами Blackjack и хранит счёт игры.
 */
public final class Game {
    private static final int INITIAL_ROUND_NUMBER = 0;

    private final int deckCount;
    private final String playerName;
    private int roundNumber = INITIAL_ROUND_NUMBER;
    private int playerWins;
    private int dealerWins;

    /**
     * Создаёт новую игру.
     *
     * @param deckCount количество колод
     * @param playerName имя игрока
     */
    public Game(int deckCount, String playerName) {
        this.deckCount = deckCount;
        this.playerName = playerName;
    }

    /**
     * Создаёт новый раунд игры.
     *
     * @return новый раунд
     */
    public Round createRound() {
        return createRound(new Deck(deckCount));
    }

    /**
     * Создаёт новый раунд с указанной колодой.
     *
     * @param deck колода для раунда
     * @return созданный раунд
     */
    Round createRound(Deck deck) {
        roundNumber++;
        Round round = new Round(deck, playerName);
        round.dealInitialCards();
        return round;
    }

    /**
     * Возвращает номер текущего раунда.
     *
     * @return номер раунда
     */
    public int getRoundNumber() {
        return roundNumber;
    }

    /**
     * Возвращает количество побед игрока.
     *
     * @return количество побед игрока
     */
    public int getPlayerWins() {
        return playerWins;
    }

    /**
     * Возвращает количество побед дилера.
     *
     * @return количество побед дилера
     */
    public int getDealerWins() {
        return dealerWins;
    }

    /**
     * Регистрирует результат завершённого раунда.
     *
     * @param result результат раунда
     */
    public void registerResult(Round.Result result) {
        if (result == Round.Result.PLAYER_WIN) {
            playerWins++;
        } else if (result == Round.Result.DEALER_WIN) {
            dealerWins++;
        }
    }

    /**
     * Возвращает текущий счёт игры в текстовом виде.
     *
     * @return строка с текущим счётом
     */
    public String getScoreText() {
        return "Счет " + playerWins + ":" + dealerWins + " в вашу пользу.";
    }
}
