package com.queens;

import com.queens.io.BoardPrinter;
import com.queens.io.PuzzleParser;
import com.queens.io.QueensFetcher;
import com.queens.model.Board;
import com.queens.model.Queen;
import com.queens.solver.BacktrackingSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length == 0) {
            System.out.println("Usage: solve-random | fetch-database");
            return;
        }

        switch (args[0]) {
            case "solve-random" -> solveRandom();
            case "fetch-database" -> QueensFetcher.fetch8x8Puzzles();
            default -> System.out.println("Unknown command: " + args[0] + "\nUsage: solve-random | fetch-database");
        }
    }

    public static void solveRandom() throws IOException {

        //list all files and choose one at random
        Random random = new Random();
        List<Path> files = Files.list(Path.of("./puzzles/8x8/")).toList();
        int randomPathID = random.nextInt(files.size());
        Path filepath = files.get(randomPathID);

        //parse the input file
        Board board = PuzzleParser.parse(String.valueOf(filepath));

        //solve the board correctly in the backgroud
        BacktrackingSolver solver = new BacktrackingSolver();
        List<Queen> correctQueens = solver.solve(board);

        //print out the solved board
        System.out.println("Solution for queens puzzle " + filepath + " :");
        System.out.println();
        BoardPrinter.printSolution(board, correctQueens);
    }
}