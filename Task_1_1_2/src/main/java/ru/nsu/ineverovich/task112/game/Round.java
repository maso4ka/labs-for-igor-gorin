package ru.nsu.ineverovich.task112.game;

/**
 * Управляет состоянием одного раунда Blackjack.
 */
public final class Round {
    private final int roundNumber;
    private boolean finished;

    /**
     * Создаёт новый раунд.
     *
     * @param roundNumber номер раунда
     */
    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
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
}