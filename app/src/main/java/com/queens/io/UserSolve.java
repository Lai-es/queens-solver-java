package com.queens.io;

import com.queens.Main;
import com.queens.model.Board;
import com.queens.model.Queen;

import java.io.IOException;
import java.util.*;

public class UserSolve {

    //---fields
    private final Board board;
    private List<Queen> userQueens = new ArrayList<>();
    private Set<String> userMarked = new HashSet<>();
    private final List<Queen> correctQueens;
    private final boolean[][] correctMarks;
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
        this.correctMarks = computeMarkedCells(correctQueens, board);
    }

    //main play function
    public void playGame() throws IOException {
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
                    "'reset' to reset the puzzle.\nType 'tip' to receive a tip for the current puzzle state." + RESET);
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
                    System.out.println(TEXT + "much love!" + RESET);
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
                int row = Integer.parseInt(String.valueOf(response[0])) - 1;
                int col = Integer.parseInt(String.valueOf(response[1])) - 1;

                boolean[][] marked = computeMarkedCells(userQueens, board);
                //check if new queen would be placed on a automatically marked field
                if (marked[row][col]) {
                    //if this is true, the cell is marked - dont append the new queen
                    System.out.println(ERROR + "You're trying to place a queen on a marked cell: row " + (row+1) + "," +
                            " " +
                            "column " + (col+1) + ".\nPlease input a valid queen" + RESET);
                } else {
                    userQueens.add(new Queen(row, col));
                    System.out.println(TEXT + "Placed a queen on row " + (row+1) + ", column " + (col+1) + RESET);
                }
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

        //give option to play another game
        endOrResetGameLoop();
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
        Printer.printQueens(board, userQueens, userMarked, computeMarkedCells(userQueens, board));
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
                return;
            }
        }
        //check if one of the user-marked cells is falsely marked
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                if (!correctMarks[row][col] && userMarked.contains(row + "," + col)) {
                    //this statement runs when in the correct solution no mark is set but the user set a mark at the
                    // current cell
                    System.out.println(TIP + "The mark you set at row " + (row+1) + ", column " + (col+1) + " " +
                            "is incorrect" + RESET);
                    return;
                }
            }
        }
        //give the next queen location
        for (Queen queen : correctQueens) {
            if (!userQueens.contains(queen)) {
                System.out.println(TIP + "The location of the next queen is row " + (queen.row()+1) + ", column " + (queen.column()+1) + RESET);
                return;
            }
        }
    }

    //function to calculate marked cells which thereby dont allow other queens to be set on the mark
    private boolean[][] computeMarkedCells(List<Queen> queens, Board board) {
        int size = board.getSize();
        boolean[][] marked = new boolean[size][size];

        for (Queen queen : queens) {
            int qRow = queen.row();
            int qCol = queen.column();
            int qRegion = board.getRegion(qRow, qCol);

            // mark entire row and column
            for (int i = 0; i < size; i++) {
                marked[qRow][i] = true;
                marked[i][qCol] = true;
            }

            // mark adjacent cells
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = qRow + dr;
                    int newCol = qCol + dc;
                    if (newRow >= 0 && newRow < size && newCol >= 0 && newCol < size) {
                        marked[newRow][newCol] = true;
                    }
                }
            }

            //mark fields same region
            for (int row = 0; row < size; row++) {
                for (int col = 0; col < size; col++) {
                    if (board.getRegion(row, col) == qRegion) {
                        marked[row][col] = true;
                    }
                }
            }
        }
        return marked;
    }

    private void endOrResetGameLoop() throws IOException {
        System.out.println();
        System.out.println(TEXT + "Do you want to solve another (type 'again') or exit the program ('exit')?" + RESET);
        String finalResponse = userInput.nextLine().trim();
        switch (finalResponse) {
            case "again":
                Main.solveRandom();
                break;
            case "exit":
                System.exit(0);
                break;
            default :
                System.out.println(ERROR + "Input could not be read, please try again" + RESET);
                endOrResetGameLoop();
        }
    }
}