package ru.nsu.ineverovich.task112.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import ru.nsu.ineverovich.task112.game.Game;
import ru.nsu.ineverovich.task112.game.Result;
import ru.nsu.ineverovich.task112.game.Round;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет вводом и выводом консольной версии игры
 * Blackjack.
 */
public final class ConsoleUi {
    private static final String DEFAULT_PLAYER_NAME = "Игрок";
    private static final int DEFAULT_DECK_COUNT = 1;
    private static final int ACTION_STAND = 0;

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Создаёт консольный интерфейс со стандартными потоками.
     */
    public ConsoleUi() {
        this(System.in, System.out);
    }

    /**
     * Создаёт консольный интерфейс с указанными потоками.
     *
     * @param input поток ввода
     * @param output поток вывода
     */
    public ConsoleUi(InputStream input, PrintStream output) {
        scanner = new Scanner(input);
        this.output = output;
    }

    /**
     * Запускает игровой цикл.
     */
    public void run() {
        printWelcome();

        int deckCount = readDeckCount();
        Game game = new Game(DEFAULT_PLAYER_NAME, deckCount);

        boolean continueGame = true;

        while (continueGame) {
            Round round = game.startRound();

            printRoundHeader(round.getRoundNumber());
            printInitialHands(game);

            playRound(game);

            continueGame = askContinue();
        }

        output.println("Игра окончена.");
        output.println(game.getScoreText());
    }

    private void playRound(Game game) {
        Result result = game.checkBlackjack();

        if (result != null) {
            printState(game, false);
            printResult(result, game);
            return;
        }

        playerTurn(game);

        if (game.isPlayerBust()) {
            Result bustResult = game.finishRound();
            printResult(bustResult, game);
            return;
        }

        dealerTurn(game);

        Result finalResult = game.finishRound();
        printResult(finalResult, game);
    }

    private void printWelcome() {
        output.println("Добро пожаловать в Блекджек!");
        output.println("--------------------------------");
    }

    private int readDeckCount() {
        output.print(
                "Введите количество колод (1 по умолчанию): ");

        String line = scanner.nextLine().trim();

        if (line.isEmpty()) {
            return DEFAULT_DECK_COUNT;
        }

        try {
            int value = Integer.parseInt(line);
            return value > 0 ? value : DEFAULT_DECK_COUNT;
        } catch (NumberFormatException exception) {
            return DEFAULT_DECK_COUNT;
        }
    }

    private void printRoundHeader(int roundNumber) {
        output.println();
        output.println("Раунд " + roundNumber);
    }

    private void printInitialHands(Game game) {
        output.println("Дилер раздал карты.");
        printState(game, true);
    }

    private void playerTurn(Game game) {
        output.println();
        output.println("Ваш ход");
        output.println("-------");

        while (true) {
            int action = readAction();

            if (action == ACTION_STAND) {
                return;
            }

            Card card = game.takeCard();

            output.println(
                    "Вы открыли карту " + card
                            + " (" + card.getValue() + ")");

            printState(game, true);

            if (game.isPlayerBust()) {
                output.println(
                        "Вы набрали больше 21. Вы проиграли раунд.");
                return;
            }
        }
    }

    private void dealerTurn(Game game) {
        output.println();
        output.println("Ход дилера");
        output.println("-------");

        final Dealer dealer = game.getDealer();
        List<Card> cards = game.playDealerTurn();

        output.println("Дилер открывает закрытую карту.");
        printState(game, false);

        for (Card card : cards) {
            output.println(
                    "Дилер берет карту " + card
                            + " (" + card.getValue() + ")");
            printState(game, false);
        }

        if (dealer.isBust()) {
            output.println("Дилер набрал больше 21.");
        }
    }

    private void printResult(Result result, Game game) {
        if (result == Result.PLAYER_WIN) {
            output.println(
                    "Вы выиграли раунд! " + game.getScoreText());
        } else if (result == Result.DEALER_WIN) {
            output.println("Дилер выиграл раунд.");
        } else {
            output.println("Ничья.");
        }
    }

    private void printState(Game game, boolean hideDealerCard) {
        User user = game.getUser();
        Dealer dealer = game.getDealer();

        output.println(
                "Ваши карты: " + user.getHand()
                        + " > " + user.getScore());

        if (hideDealerCard && dealer.isHiddenCardPresent()) {
            output.println(
                    "Карты дилера: " + dealer.getHand()
                            + ", <закрытая карта>");
        } else {
            output.println(
                    "Карты дилера: " + dealer.getHand()
                            + " > " + dealer.getScore());
        }
    }

    private int readAction() {
        while (true) {
            output.print(
                    "Введите \"1\", чтобы взять карту, и \"0\", "
                            + "чтобы остановиться: ");

            String input = scanner.nextLine().trim();

            if ("0".equals(input) || "1".equals(input)) {
                return Integer.parseInt(input);
            }

            output.println("Введите только 1 или 0.");
        }
    }

    private boolean askContinue() {
        while (true) {
            output.print(
                    "Сыграть следующий раунд? (1 - да, 0 - нет): ");

            String input = scanner.nextLine().trim();

            if ("1".equals(input)) {
                return true;
            }

            if ("0".equals(input)) {
                return false;
            }

            output.println("Введите только 1 или 0.");
        }
    }
}