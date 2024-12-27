package com.chess.patterns;

import com.chess.board.Square;

public interface MovementStrategy {
    boolean isValidMove(Square from, Square to, Square[][] board);
}
