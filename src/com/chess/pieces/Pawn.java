package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class Pawn extends Piece {
    public Pawn(Player player) {
        super(player, player == Player.WHITE ? "WPawn.png" : "BPawn.png");
        this.movementStrategy = new PawnMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "Pawn";
    }
}

class PawnMovementStrategy implements MovementStrategy {
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        int direction = (from.getPiece().getPlayer() == Player.WHITE) ? -1 : 1;
        int rowDiff = to.getRow() - from.getRow();
        int colDiff = Math.abs(to.getCol() - from.getCol());
        
        // Basic one square forward move
        if (colDiff == 0 && rowDiff == direction && to.getPiece() == null) {
            return true;
        }
        
        // Initial two square move
        if (colDiff == 0 && 
            ((from.getPiece().getPlayer() == Player.WHITE && from.getRow() == 6 && rowDiff == -2) ||
             (from.getPiece().getPlayer() == Player.BLACK && from.getRow() == 1 && rowDiff == 2)) &&
            to.getPiece() == null && 
            board[from.getRow() + direction][from.getCol()].getPiece() == null) {
            return true;
        }
        
        // Capture move
        if (colDiff == 1 && rowDiff == direction && to.getPiece() != null && 
            to.getPiece().getPlayer() != from.getPiece().getPlayer()) {
            return true;
        }
        
        return false;
    }
}
