package com.queens.io;

import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.List;
import java.util.Set;

public class Printer {

    private static final String[] COLORS = {
            "\u001B[41m", // red
            "\u001B[42m", // green
            "\u001B[43m", // yellow
            "\u001B[44m", // blue
            "\u001B[45m", // magenta
            "\u001B[46m", // cyan
            "\u001B[47m", // white
            "\u001B[100m" // dark gray
    };
    private static final String RESET = "\u001B[0m";
    public static final String CLEAR = "\\033[H\\033[2J";

    public static void printQueens(Board board, List<Queen> queens, Set<String> userMarked) {
        //compute marked cells
        boolean[][] marked = computeMarkedCells(queens, board.getSize());

        //header
        System.out.print("  ");
        for (int col = 0; col < board.getSize(); col++) {
            System.out.print((col + 1) + " ");
        }
        System.out.println();

        //rows
        for (int row = 0; row < board.getSize(); row++) {
            System.out.print((row + 1) + " ");
            Queen queen = findQueenInRow(queens, row);
            for (int col = 0; col < board.getSize(); col++) {
                boolean isQueen = queen != null && queen.row() == row && queen.column() == col; //is the current cell a queen?
                int region = board.getRegion(row, col); //1, 2...8
                String color = COLORS[region]; //maps each region to a color
                String content = isQueen ? "Q " : ((marked[row][col] || userMarked.contains(row + "," + col)) ? "X " : (region + " "));
                System.out.print(color + content + RESET);
            }
            System.out.println();
        }

        //buffer newline
        System.out.println();
    }

    private static Queen findQueenInRow(List<Queen> queens, int row) {
        for (Queen queen : queens) {
            if (queen.row() == row) return queen;
        }
        return null;
    }

    private static boolean[][] computeMarkedCells(List<Queen> queens, int size) {
        boolean[][] marked = new boolean[size][size];

        for (Queen queen : queens) {
            int qRow = queen.row();
            int qCol = queen.column();

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
        }

        return marked;
    }
}