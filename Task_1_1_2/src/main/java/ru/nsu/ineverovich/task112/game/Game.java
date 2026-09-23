package ru.nsu.ineverovich.task112.game;

import java.util.ArrayList;
import java.util.List;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет состоянием игры и её участниками.
 */
public final class Game {
    private final User user;
    private final Dealer dealer;
    private int playerWins;
    private int dealerWins;

    /**
     * Создаёт новую игру.
     *
     * @param playerName имя игрока
     */
    public Game(String playerName) {
        user = new User(playerName);
        dealer = new Dealer();
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
     * Проверяет наличие Blackjack у участников.
     *
     * @param round текущий раунд
     * @return результат, если Blackjack уже определяет исход,
     *         иначе {@code null}
     */
    public Result checkBlackjack(Round round) {
        if (!user.hasBlackjack() && !dealer.hasHiddenBlackjack()) {
            return null;
        }
        dealer.revealHiddenCard();
        return finishRound(round);
    }

    /**
     * Даёт игроку следующую карту.
     *
     * @param round текущий раунд
     * @return полученная карта
     */
    public Card takeCard(Round round) {
        Card card = round.drawCard();
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
     * @param round текущий раунд
     * @return карты, которые дилер открыл в этом ходе
     */
    public List<Card> playDealerTurn(Round round) {
        List<Card> drawnCards = new ArrayList<>();
        dealer.revealHiddenCard();
        while (dealer.getScore() < Dealer.STAND_SCORE) {
            Card card = round.drawCard();
            dealer.receiveCard(card);
            drawnCards.add(card);
            if (dealer.isBust()) {
                break;
            }
        }
        return drawnCards;
    }

    /**
     * Определяет и регистрирует результат раунда.
     *
     * @param round завершённый раунд
     * @return результат раунда
     */
    public Result finishRound(Round round) {
        Result result = round.determineResult();
        registerResult(result);
        round.finish();
        return result;
    }

    /**
     * Регистрирует результат завершённого раунда.
     *
     * @param result результат раунда
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
        return "Счет " + playerWins + ":" + dealerWins + " в вашу пользу.";
    }
}
