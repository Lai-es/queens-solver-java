package com.queens.io;

import com.queens.model.Board;
import com.queens.model.Queen;

import java.util.ArrayList;
import java.util.List;

public class UserSolve {

    private final Board board;
    private List<Queen> queens = new ArrayList<>();

    public UserSolve (Board board) {
        this.board = board;
    }

    public void printGame(Board board, List<Queen> queens) {

    }
}