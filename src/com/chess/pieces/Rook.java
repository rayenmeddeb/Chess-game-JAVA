package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class Rook extends Piece {
    public Rook(Player player) {
        super(player, player == Player.WHITE ? "WRook.png" : "BRook.png");
        this.movementStrategy = new RookMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "Rook";
    }
}

class RookMovementStrategy implements MovementStrategy {
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        if (from.getRow() != to.getRow() && from.getCol() != to.getCol()) {
            return false;
        }
        
        int rowDir = Integer.compare(to.getRow(), from.getRow());
        int colDir = Integer.compare(to.getCol(), from.getCol());
        
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        
        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            if (board[currentRow][currentCol].getPiece() != null) {
                return false;
            }
            currentRow += rowDir;
            currentCol += colDir;
        }
        
        return true;
    }
}
