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

    /**
     * Создаёт дилера.
     */
    public Dealer() {
        super("Дилер");
    }
}
