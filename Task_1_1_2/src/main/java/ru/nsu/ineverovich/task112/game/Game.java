package ru.nsu.ineverovich.task112.game;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.Deck;
import ru.nsu.ineverovich.task112.model.DeckConstants;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет состоянием игры и её участниками.
 */
public final class Game {
    private static final int FIRST_ROUND_NUMBER = 1;
    private static final int INITIAL_CARD_COUNT = 2;

    private final User user;
    private final Dealer dealer;
    private final Deck deck;

    private int playerWins;
    private int dealerWins;
    private int nextRoundNumber = FIRST_ROUND_NUMBER;
    private Round currentRound;

    /**
     * Создаёт новую игру.
     *
     * @param playerName имя игрока
     * @param deckCount количество колод
     */
    public Game(String playerName, int deckCount) {
        user = new User(playerName);
        dealer = new Dealer();
        deck = new Deck(deckCount);
    }

    /**
     * Создаёт новую игру с одной стандартной колодой.
     *
     * @param playerName имя игрока
     */
    public Game(String playerName) {
        this(playerName, DeckConstants.STANDARD_DECK_COUNT);
    }

    /**
     * Возвращает игрока игры.
     *
     * @return игрок
     */
    public User getUser() {
        return user;
    }

    /**
     * Возвращает дилера игры.
     *
     * @return дилер
     */
    public Dealer getDealer() {
        return dealer;
    }

    /**
     * Возвращает текущий раунд.
     *
     * @return текущий раунд
     */
    public Round getCurrentRound() {
        return currentRound;
    }

    /**
     * Начинает новый раунд.
     *
     * @return созданный раунд
     */
    public Round startRound() {
        user.resetHand();
        dealer.resetHand();

        currentRound = new Round(nextRoundNumber);
        nextRoundNumber++;

        dealInitialCards();

        return currentRound;
    }

    private void dealInitialCards() {
        for (int cardIndex = 0;
             cardIndex < INITIAL_CARD_COUNT;
             cardIndex++) {
            user.receiveCard(deck.draw());

            if (cardIndex == INITIAL_CARD_COUNT - 1) {
                dealer.receiveHiddenCard(deck.draw());
            } else {
                dealer.receiveCard(deck.draw());
            }
        }
    }

    /**
     * Проверяет наличие Blackjack у участников.
     *
     * @return результат, если Blackjack уже определяет исход,
     *         иначе {@code null}
     */
    public Result checkBlackjack() {
        if (!user.hasBlackjack() && !dealer.hasHiddenBlackjack()) {
            return null;
        }

        dealer.revealHiddenCard();
        return finishRound();
    }

    /**
     * Даёт игроку следующую карту.
     *
     * @return полученная карта
     */
    public Card takeCard() {
        Card card = deck.draw();
        user.receiveCard(card);
        return card;
    }

    /**
     * Проверяет, закончен ли ход игрока из-за перебора.
     *
     * @return {@code true}, если игрок перебрал
     */
    public boolean isPlayerBust() {
        return user.isBust();
    }

    /**
     * Выполняет ход дилера.
     *
     * @return карты, которые дилер взял в этом ходе
     */
    public List<Card> playDealerTurn() {
        List<Card> drawnCards = new ArrayList<>();

        dealer.revealHiddenCard();

        while (dealer.getScore() < Dealer.STAND_SCORE) {
            Card card = deck.draw();
            dealer.receiveCard(card);
            drawnCards.add(card);

            if (dealer.isBust()) {
                break;
            }
        }

        return drawnCards;
    }

    /**
     * Определяет и регистрирует результат текущего раунда.
     *
     * @return результат раунда
     */
    public Result finishRound() {
        Result result = determineResult();
        registerResult(result);
        currentRound.finish();
        return result;
    }

    private Result determineResult() {
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

    /**
     * Регистрирует результат завершённого раунда.
     *
     * @param result результат
     */
    public void registerResult(Result result) {
        if (result == Result.PLAYER_WIN) {
            playerWins++;
        } else if (result == Result.DEALER_WIN) {
            dealerWins++;
        }
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
     * Возвращает текущий счёт игры в текстовом виде.
     *
     * @return строка с текущим счётом
     */
    public String getScoreText() {
        return "Счет " + playerWins + ":" + dealerWins
                + " в вашу пользу.";
    }
}