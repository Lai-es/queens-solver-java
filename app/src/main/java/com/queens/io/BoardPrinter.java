package com.queens.io;

import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.List;

public class BoardPrinter {

    public static void printBoard(Board board) {
        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {
                System.out.print(board.getRegion(row, col) + " ");
            }
            System.out.println();
        }
    }

    public static void printSolution(Board board, List<Queen> queens) {
        for (int row = 0; row < board.getSize(); row++) {
            Queen queen = queens.get(row);
            for (int col = 0; col < board.getSize(); col++) {
                System.out.print( (queen.row() == row && queen.column() == col) ? "Q " : board.getRegion(row, col) +
                                                                                        " ");
            }
            System.out.println();
        }
    }
}