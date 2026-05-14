package com.queens.io;

import com.queens.model.Board;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class PuzzleParser {

    public static Board parse(String filePath) throws IOException {
        //read all lines of input file
        List<String> parsed = Files.readAllLines(Path.of(filePath));
        int size = parsed.size();
        int[][] board = new int[size][size];
        //iterate over ever row (list entry)
        for (int row = 0; row < size; row++) {
            //split values on " " and parse into string array
            String[] numbers = parsed.get(row).split(" ");
            for (int col = 0; col < size; col++) {
                board[row][col] = Integer.parseInt(numbers[col]);
            }
        }
        return new Board(board);
    }
}