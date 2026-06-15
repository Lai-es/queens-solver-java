package com.queens.io;

import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.*;

public class UserSolve {

    //fields
    private final Board board;
    private List<Queen> userQueens = new ArrayList<>();
    private final List<Queen> correctQueens;
    private Set<String> userMarked = new HashSet<>();
    private final int[] dictionaryRows = {1, 2, 3, 4, 5, 6, 7, 8};
    private final int[] dictionaryCols = {1, 2, 3, 4, 5, 6, 7, 8};

    //listener
    private final Scanner userInput = new Scanner(System.in);

    //constructor
    public UserSolve (Board board, List<Queen> correctQueens) {
        this.board = board;
        this.correctQueens = correctQueens;
    }

    //main play function
    public void playGame() {
        //print fresh board once
        Printer.printQueens(board, new ArrayList<>(), userMarked);
        long startTime = System.currentTimeMillis();

        //play until all queens are correctly placed
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
                System.out.printf("Time: %d min, %d seconds%n", minutes, seconds);
            } else {
                System.out.printf("Time: %d seconds%n", seconds);
            }
            System.out.println();

            //print instructions
            System.out.println("Enter a position (e.g. 11 for row 1, col 1), or r11 to add/remove a queen or x11 to mark a no-queen spot\nType 'solve' for an instant solution. Type 'rules' to see the rules");
            System.out.println();
            String responseString = userInput.nextLine();

            //user gives up
            if (responseString.equals("solve")) {
                userQueens = correctQueens;
                Printer.printQueens(board, userQueens, userMarked);
                break;
            }

            if (responseString.equals("rules")) {
                System.out.println("Rules:");
                System.out.println("1. Each row must contain exactly one queen");
                System.out.println("2. Each column must contain exactly one queen");
                System.out.println("3. Each color region must contain exactly one queen");
                System.out.println("4. No two queens may touch — not even diagonally");
                Printer.printQueens(board, userQueens, userMarked);
                continue;
            }

            char[] response = responseString.toCharArray();


            // removal case
            if (response.length == 3 && response[0] == 'r' && validateInput(response[1], response[2])) {
                int row = Integer.parseInt(String.valueOf(response[1])) - 1;
                int col = Integer.parseInt(String.valueOf(response[2])) - 1;
                userQueens.removeIf(q -> q.row() == row && q.column() == col);
                Printer.printQueens(board, userQueens, userMarked);
            }

            // mark case
            else if (response.length == 3 && response[0] == 'x' && validateInput(response[1], response[2])) {
                int row = Integer.parseInt(String.valueOf(response[1])) - 1;
                int col = Integer.parseInt(String.valueOf(response[2])) - 1;
                String key = row + "," + col;
                if (userMarked.contains(key)) {
                    userMarked.remove(key);
                } else {
                    userMarked.add(key);
                }
                Printer.printQueens(board, userQueens, userMarked);
            }
            //placement case
            else if (response.length == 2 && validateInput(response[0], response[1])) {
                int col = Integer.parseInt(String.valueOf(response[1])) - 1;
                int row = Integer.parseInt(String.valueOf(response[0])) - 1;
                userQueens.add(new Queen(row, col));
                Printer.printQueens(board, userQueens, userMarked);
            }

            //default/parsing error
            else {
                System.out.println("Input could not be read, please try again");
                Printer.printQueens(board, userQueens, userMarked);
            }
        }
        //User solved the board correctly
        long elapsed = System.currentTimeMillis() - startTime;
        long seconds = elapsed / 1000;
        long minutes = seconds / 60;
        if (minutes > 0) {
            seconds = seconds % 60;
            System.out.printf("Congratulations, you solved the board correctly in %d min, %d seconds. Good Job!%n", minutes, seconds);
        } else {
            System.out.printf("Congratulations, you solved the board correctly in %d seconds. Good Job!%n", seconds);
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
}