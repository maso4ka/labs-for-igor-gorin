package ru.nsu.ineverovich.Task_1_1_2.game;

import ru.nsu.ineverovich.Task_1_1_2.model.Deck;

public final class Game {
    private static final int INITIAL_ROUND_NUMBER = 0;

    private final int deckCount;
    private final String playerName;
    private int roundNumber = INITIAL_ROUND_NUMBER;
    private int playerWins;
    private int dealerWins;

    public Game(int deckCount, String playerName) {
        this.deckCount = deckCount;
        this.playerName = playerName;
    }

    public Round createRound() {
        return createRound(new Deck(deckCount));
    }

    Round createRound(Deck deck) {
        roundNumber++;
        Round round = new Round(deck, playerName);
        round.dealInitialCards();
        return round;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public int getPlayerWins() {
        return playerWins;
    }

    public int getDealerWins() {
        return dealerWins;
    }

    public void registerResult(Round.Result result) {
        if (result == Round.Result.PLAYER_WIN) {
            playerWins++;
        } else if (result == Round.Result.DEALER_WIN) {
            dealerWins++;
        }
    }

    public String getScoreText() {
        return "Счет " + playerWins + ":" + dealerWins + " в вашу пользу.";
    }
}
