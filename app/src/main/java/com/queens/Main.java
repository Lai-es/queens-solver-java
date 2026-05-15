package com.queens;

import com.queens.io.BoardPrinter;
import com.queens.io.PuzzleParser;
import com.queens.model.Board;
import com.queens.model.Queen;
import com.queens.solver.BacktrackingSolver;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String filepath = "/home/er/Projects/Queens-LinkedIn-solver-java/puzzle.txt";
        //parse the input file
        Board board = PuzzleParser.parse(filepath);
        //print the board
        BoardPrinter.printBoard(board);
        //solve the board
        BacktrackingSolver solver = new BacktrackingSolver();
        List<Queen> queens= solver.solve(board);
        //print out the solved board
        BoardPrinter.printSolution(board, queens);
    }
}