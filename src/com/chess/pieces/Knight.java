package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class Knight extends Piece {
    public Knight(Player player) {
        super(player, player == Player.WHITE ? "WKnight.png" : "BKnight.png");
        this.movementStrategy = new KnightMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "Knight";
    }
}

class KnightMovementStrategy implements MovementStrategy {
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
}
