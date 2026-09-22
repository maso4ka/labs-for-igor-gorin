package ru.nsu.ineverovich.Task_1_1_2.game;

import ru.nsu.ineverovich.Task_1_1_2.model.Card;
import ru.nsu.ineverovich.Task_1_1_2.model.Dealer;
import ru.nsu.ineverovich.Task_1_1_2.model.Deck;
import ru.nsu.ineverovich.Task_1_1_2.model.User;

public final class Round {
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

    public Round(Deck deck, String playerName) {
        if (deck == null) {
            throw new IllegalArgumentException("Колода не может быть null");
        }
        this.deck = deck;
        user = new User(playerName);
        dealer = new Dealer();
    }

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

    public Card drawCard() {
        return deck.draw();
    }

    public User getUser() {
        return user;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Card getHiddenDealerCard() {
        return hiddenDealerCard;
    }

    public void revealDealerCard() {
        if (hiddenDealerCard != null) {
            dealer.receiveCard(hiddenDealerCard);
            hiddenDealerCard = null;
        }
    }

    public boolean isDealerCardHidden() {
        return hiddenDealerCard != null;
    }

    public boolean isFinished() {
        return finished;
    }

    public void finish() {
        finished = true;
    }

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

