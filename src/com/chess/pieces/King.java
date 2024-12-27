package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class King extends Piece {
    public King(Player player) {
        super(player, player == Player.WHITE ? "WKing.png" : "BKing.png");
        this.movementStrategy = new KingMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "King";
    }
}

class KingMovementStrategy implements MovementStrategy {
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        return rowDiff <= 1 && colDiff <= 1;
    }
}
