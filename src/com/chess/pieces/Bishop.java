package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class Bishop extends Piece {
    public Bishop(Player player) {
        super(player, player == Player.WHITE ? "WBishop.png" : "BBishop.png");
        this.movementStrategy = new BishopMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "Bishop";
    }
}

class BishopMovementStrategy implements MovementStrategy {
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        int rowDiff = Math.abs(to.getRow() - from.getRow());
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        if (rowDiff != colDiff) {
            return false;
        }
        
        int rowDir = Integer.compare(to.getRow(), from.getRow());
        int colDir = Integer.compare(to.getCol(), from.getCol());
        
        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;
        
        while (currentRow != to.getRow() && currentCol != to.getCol()) {
            if (board[currentRow][currentCol].getPiece() != null) {
                return false;
            }
            currentRow += rowDir;
            currentCol += colDir;
        }
        
        return true;
    }
}
