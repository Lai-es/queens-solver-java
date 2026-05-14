package com.queens.validator;

import java.util.List;
import com.queens.model.Board;
import com.queens.model.Queen;

public class PlacementValidator {

    public boolean isValid(Board board, List<Queen> placedQueens, int row, int column) {
        return isColumnValid(placedQueens, column) && isRowValid(placedQueens, row) && isRegionValid(board, placedQueens
                , row, column) && isAdjacentValid(placedQueens, row, column);
        }

    //helper method to check if row is valid
    private boolean isRowValid(List<Queen> placedQueens, int row) {
        for (Queen queen : placedQueens) {
            if (queen.row() == row) return false;
        }
        return true;
    }

    //helper method to check if column is valid
    private boolean isColumnValid(List<Queen> placedQueens, int column) {
        for (Queen queen : placedQueens) {
            if (queen.column() == column) return false;
        }
        return true;
    }

    //helper method to check if region is valid
    private boolean isRegionValid(Board board, List<Queen> placedQueens, int row, int column) {
        int currentRegion = board.getRegion(row, column);
        for (Queen queen : placedQueens) {
            if (board.getRegion(queen.row(), queen.column()) == currentRegion) return false;
        }
        return true;
    }

    //helper method to check if no adjacent squares are occupied
    private boolean isAdjacentValid(List<Queen> placedQueens, int row, int column) {
        for (Queen queen : placedQueens) {
            int rowDiff = Math.abs(queen.row() - row);
            int colDiff = Math.abs(queen.column() - column);
            if (rowDiff <= 1 && colDiff <= 1 && !(rowDiff == 0 && colDiff == 0)) return false;
        }
        return true;
    }

}