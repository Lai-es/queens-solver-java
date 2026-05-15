package com.queens.solver;

import com.queens.validator.PlacementValidator;
import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.ArrayList;
import java.util.List;

public class BacktrackingSolver {

    ArrayList<Queen> queens;
    PlacementValidator validator = new PlacementValidator();

    public List<Queen> solve(Board board) {
        queens = new ArrayList<>();
        System.out.println("Starting solve, board size: " + board.getSize());
        solve(board, queens, 0);
        return queens;
    }

    private boolean solve(Board board, List<Queen> placedQueens, int currentRow) {
        if (currentRow == board.getSize()) { // base case, every row was looped through
            return true;
        } else {
            for (int column = 0; column < board.getSize(); column++) { //iterate over the columns for each row
                //check if current column in row is a valid position
                if (validator.isValid(board, placedQueens, currentRow, column)) {
                    placedQueens.add(new Queen(currentRow, column));
                    //check if it works for the recursion cases too or if it blocks a queen
                    if (solve(board, placedQueens, currentRow + 1)) {return true;}
                    //if a queen downstream is blocked, remove current queen and try the next column
                    else {placedQueens.removeLast();}
                    }
                }
            return false;
            }
        }
    }
