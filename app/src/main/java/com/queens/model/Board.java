package com.queens.model;

public class Board {
    private int size;
    private int[][] board;

    //constructor taking in the color grid
    public Board(int[][] grid) {
        this.size = grid.length;
        this.board = grid;
    }

    //getter for size as well as number of regions
    public int getSize() {
        return size;
    }

    //getter for region/color at row/column index
    public int getRegion(int row, int column) {
        return board[row][column];
    }
}