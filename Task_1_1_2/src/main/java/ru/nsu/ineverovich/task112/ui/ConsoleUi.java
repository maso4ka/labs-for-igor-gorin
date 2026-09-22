package ru.nsu.ineverovich.task112.ui;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.Scanner;

import ru.nsu.ineverovich.task112.game.Game;
import ru.nsu.ineverovich.task112.game.Round;
import ru.nsu.ineverovich.task112.model.Card;
import ru.nsu.ineverovich.task112.model.Dealer;
import ru.nsu.ineverovich.task112.model.User;

/**
 * Управляет взаимодействием игрока с консольной
 * версией игры Blackjack.
 */
public final class ConsoleUi {
    private static final String DEFAULT_PLAYER_NAME = "Игрок";
    private static final int DEFAULT_DECK_COUNT = 1;
    private static final int ACTION_STAND = 0;

    private final Scanner scanner;
    private final PrintStream output;

    /**
     * Создаёт консольный интерфейс со стандартными
     * потоками ввода и вывода.
     */
    public ConsoleUi() {
        this(System.in, System.out);
    }

    /**
     * Создаёт консольный интерфейс с указанными
 * потоками ввода и вывода.
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
        Game game = new Game(deckCount, DEFAULT_PLAYER_NAME);
        boolean continueGame = true;
        while (continueGame) {
            Round round = game.createRound();
            printRoundHeader(game.getRoundNumber());
            printInitialHands(round);
            playRound(round, game);
            continueGame = askContinue();
        }
        output.println("Игра окончена.");
        output.println(game.getScoreText());
    }

    private void playRound(Round round, Game game) {
        if (!handleBlackjacks(round, game)) {
            playerTurn(round);
            if (!round.getUser().isBust()) {
                dealerTurn(round);
            }
            if (!round.isFinished()) {
                finishRound(round, game);
            }
        }
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

    private void printInitialHands(Round round) {
        output.println("Дилер раздал карты.");
        printState(round, true);
    }

    private boolean handleBlackjacks(Round round, Game game) {
        final User user = round.getUser();
        final Dealer dealer = round.getDealer();
        if (!user.hasBlackjack() && !dealer.hasBlackjack()) {
            return false;
        }
        round.revealDealerCard();
        printState(round, false);
        Round.Result result = round.determineResult();
        game.registerResult(result);
        printResult(result, game);
        round.finish();
        return true;
    }

    private void playerTurn(Round round) {
        final User user = round.getUser();
        output.println();
        output.println("Ваш ход");
        output.println("-------");
        while (true) {
            int action = readAction();
            if (action == ACTION_STAND) {
                return;
            }
            Card card = round.drawCard();
            user.receiveCard(card);
            output.println("Вы открыли карту " + card + " (" + card.getValue() + ")");
            printState(round, true);
            if (user.isBust()) {
                output.println(
                        "Вы набрали больше 21. Вы проиграли раунд.");
                return;
            }
        }
    }

    private void dealerTurn(Round round) {
        final Dealer dealer = round.getDealer();
        output.println();
        output.println("Ход дилера");
        output.println("-------");
        round.revealDealerCard();
        output.println("Дилер открывает закрытую карту.");
        printState(round, false);
        while (dealer.getScore() < Dealer.STAND_SCORE) {
            Card card = round.drawCard();
            dealer.receiveCard(card);
            output.println(
                    "Дилер открывает карту " + card + " ("
                            + card.getValue() + ")");
            printState(round, false);
            if (dealer.isBust()) {
                output.println("Дилер набрал больше 21.");
                return;
            }
        }
    }

    private void finishRound(Round round, Game game) {
        Round.Result result = round.determineResult();
        game.registerResult(result);
        printResult(result, game);
        round.finish();
    }

    private void printResult(Round.Result result, Game game) {
        if (result == Round.Result.PLAYER_WIN) {
            output.println("Вы выиграли раунд! " + game.getScoreText());
        } else if (result == Round.Result.DEALER_WIN) {
            output.println("Дилер выиграл раунд.");
        } else {
            output.println("Ничья.");
        }
    }

    private void printState(Round round, boolean hideDealerCard) {
        final User user = round.getUser();
        final Dealer dealer = round.getDealer();
        output.println("Ваши карты: " + user.getHand() + " > " + user.getScore());
        if (hideDealerCard && round.isDealerCardHidden()) {
            output.println(
                    "Карты дилера: " + dealer.getHand()
                            + ", <закрытая карта>");
        } else {
            output.println(
                    "Карты дилера: " + dealer.getHand() + " > "
                            + dealer.getScore());
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
            output.print("Сыграть следующий раунд? (1 - да, 0 - нет): ");
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
