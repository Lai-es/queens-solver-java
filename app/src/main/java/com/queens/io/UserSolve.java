package com.queens.io;

import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.*;

public class UserSolve {

    //---fields
    private final Board board;
    private List<Queen> userQueens = new ArrayList<>();
    private final List<Queen> correctQueens;
    private Set<String> userMarked = new HashSet<>();
    private final int[] dictionaryRows = {1, 2, 3, 4, 5, 6, 7, 8};
    private final int[] dictionaryCols = {1, 2, 3, 4, 5, 6, 7, 8};
    //listener
    private final Scanner userInput = new Scanner(System.in);
    //Error/Reset ANSI chars
    public static final String ERROR = "\u001B[31m"; //RED foreground
    private final String TEXT = "\u001B[35m"; //MAGENTA foreground
    private final String RESET = Printer.RESET;
    private final String TIP = "\u001B[33m"; //Yellow foreground

    //constructor
    public UserSolve (Board board, List<Queen> correctQueens) {
        this.board = board;
        this.correctQueens = correctQueens;
    }

    //main play function
    public void playGame() {
        //print fresh board once and start the timer
        printGame();
        long startTime = System.currentTimeMillis();

        //---Game loop
        //play until all queens are correctly placed
        label:
        while (!isSolutionCorrect(correctQueens, userQueens)) {
            //clear all lines before in colsole
            //System.out.println(Printer.CLEAR);
            //System.out.flush();

            //calculate and display time since start
            long elapsed = System.currentTimeMillis() - startTime;
            long seconds = elapsed / 1000;
            long minutes = seconds / 60;
            if (minutes > 0) {
                seconds = seconds % 60;
                System.out.printf("Time: %d min and %d seconds%n", minutes, seconds);
            } else {
                System.out.printf("Time: %d seconds%n", seconds);
            }
            System.out.println();

            //print instructions
            System.out.println(TEXT +"Enter a position (e.g. 11 for row 1, col 1), or r11 to add/remove a queen or " +
                    "x11 to " +
                    "mark a no-queen spot\nType 'solve' for an instant solution. Type 'rules' to see the rules. Type " +
                    "'reset' to reset the puzzle" + RESET);
            System.out.println();

            //user response
            String responseString = userInput.nextLine().strip();

            //named responses
            switch (responseString) {
                case "tip": //give user a game tip
                    giveTip();
                    printGame();
                    continue;

                case "solve"://automatically solve and show game
                    userQueens = correctQueens;
                    printGame();
                    break label;

                case "love"://Easter egg
                    System.out.println("much love!");
                    System.out.println();
                    printGame();
                    continue;

                case "reset"://reset game state
                    System.out.println(ERROR + "Resetted the puzzle:" + RESET);
                    userQueens = new ArrayList<>();
                    startTime = System.currentTimeMillis();
                    printGame();
                    continue;

                case "rules": //rules
                    System.out.println(TEXT + "Rules:");
                    System.out.println("1. Each row must contain exactly one queen");
                    System.out.println("2. Each column must contain exactly one queen");
                    System.out.println("3. Each color region must contain exactly one queen");
                    System.out.println("4. No two queens may touch — not even diagonally" + RESET);
                    printGame();
                    continue;
            }

            //parse response to CharArray to better index characters
            char[] response = responseString.toCharArray();


            // removal case
            if (response.length == 3 && (response[0] == 'r' || response[0] == 'R') && validateInput(response[1],
                    response[2])) {
                int row = Integer.parseInt(String.valueOf(response[1])) - 1;
                int col = Integer.parseInt(String.valueOf(response[2])) - 1;
                userQueens.removeIf(q -> q.row() == row && q.column() == col);
                printGame();
            }

            // mark case
            else if (response.length == 3 && (response[0] == 'x' || response[0] == 'X') && validateInput(response[1],
                    response[2])) {
                int row = Integer.parseInt(String.valueOf(response[1])) - 1;
                int col = Integer.parseInt(String.valueOf(response[2])) - 1;
                String key = row + "," + col;
                if (userMarked.contains(key)) {
                    userMarked.remove(key);
                } else {
                    userMarked.add(key);
                }
                printGame();
            }
            //placement case
            else if (response.length == 2 && validateInput(response[0], response[1])) {
                int col = Integer.parseInt(String.valueOf(response[1])) - 1;
                int row = Integer.parseInt(String.valueOf(response[0])) - 1;
                userQueens.add(new Queen(row, col));
                printGame();
            }

            //default/parsing error
            else {
                System.out.println(ERROR + "Input could not be read, please try again" + RESET);
                printGame();
            }
        }
        //---finished game loop
        //User solved the board correctly
        long elapsed = System.currentTimeMillis() - startTime;
        long seconds = elapsed / 1000;
        long minutes = seconds / 60;
        if (minutes > 0) {
            seconds = seconds % 60;
            System.out.printf(TEXT + "Congratulations, you solved the board correctly in %d min and %d seconds. Good " +
                    "Job!%n" + RESET, minutes, seconds);
        } else {
            System.out.printf(TEXT + "Congratulations, you solved the board correctly in %d seconds. Good Job!%n" + RESET, seconds);
        }
        System.exit(0);
    }

    //helper method to check if all queens were placed correctly
    private boolean isSolutionCorrect(List<Queen> correctQueens, List<Queen> userQueens) {
        return new HashSet<>(correctQueens).equals(new HashSet<>(userQueens));
    }

    //helper to validate the user Input
    private boolean validateInput(char inputRow, char inputCol) {
        //check if coordinates are valid
        boolean rowsOK = false;
        boolean colsOK = false;

        for (int c : dictionaryRows) {
            if (c == Integer.parseInt(String.valueOf(inputRow))) {
                rowsOK = true;
                break;
            }
        }
        for (int c : dictionaryCols) {
            if (c == Integer.parseInt(String.valueOf(inputCol))) {
                colsOK = true;
                break;
            }
        }

        return colsOK && rowsOK;
    }

    //helper print function
    private void printGame() {
        Printer.printQueens(board, userQueens, userMarked);
        System.out.println();
    }

    //function to give the user a text-based tip depending on the game state
    private void giveTip() {
        //check if all queens so far have been correctly placed
        for (Queen queen : userQueens) {
            if (!correctQueens.contains(queen)) {
                System.out.println(TIP + "The queen at row " + (queen.row() + 1) + ", column " + (queen.column() + 1) +
                        " is " +
                        "incorrectly placed" + RESET);
                break;
            }
        }
    }
}