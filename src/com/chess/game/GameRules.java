package com.chess.game;

import com.chess.board.Square;
import com.chess.pieces.King;
import com.chess.pieces.Piece;
import com.chess.players.Player;

public class GameRules {
    private static GameRules instance;
    
    private GameRules() {}
    
    public static GameRules getInstance() {
        if (instance == null) {
            instance = new GameRules();
        }
        return instance;
    }
    
    public boolean isKingInCheck(Player player, Square[][] board) {
        // Find the king's position
        Square kingSquare = findKing(player, board);
        if (kingSquare == null) return false;
        
        // Check if any opponent's piece can capture the king
        return canAnyPieceReachSquare(getOpponent(player), kingSquare, board);
    }
    
    public boolean isCheckmate(Player player, Square[][] board) {
        if (!isKingInCheck(player, board)) {
            return false;
        }
        
        // If king is in check and has no valid moves, it's checkmate
        return !hasAnyValidMove(player, board);
    }
    
    public boolean isStalemate(Player player, Square[][] board) {
        if (isKingInCheck(player, board)) {
            return false;
        }
        
        // If king is not in check but has no valid moves, it's stalemate
        return !hasAnyValidMove(player, board);
    }
    
    private boolean hasAnyValidMove(Player player, Square[][] board) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Square fromSquare = board[fromRow][fromCol];
                if (fromSquare.getPiece() != null && fromSquare.getPiece().getPlayer() == player) {
                    if (hasValidMoves(fromSquare, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean hasValidMoves(Square fromSquare, Square[][] board) {
        for (int toRow = 0; toRow < 8; toRow++) {
            for (int toCol = 0; toCol < 8; toCol++) {
                Square toSquare = board[toRow][toCol];
                if (isValidMove(fromSquare, toSquare, board)) {
                    // Try the move and see if it leaves king in check
                    if (!wouldMoveLeaveKingInCheck(fromSquare, toSquare, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean wouldMoveLeaveKingInCheck(Square from, Square to, Square[][] board) {
        // Save current state
        Piece originalFromPiece = from.getPiece();
        Piece originalToPiece = to.getPiece();
        
        // Make move
        to.setPiece(from.getPiece());
        from.setPiece(null);
        
        // Check if king is in check
        boolean kingInCheck = isKingInCheck(originalFromPiece.getPlayer(), board);
        
        // Restore original state
        from.setPiece(originalFromPiece);
        to.setPiece(originalToPiece);
        
        return kingInCheck;
    }
    
    private Square findKing(Player player, Square[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col].getPiece();
                if (piece instanceof King && piece.getPlayer() == player) {
                    return board[row][col];
                }
            }
        }
        return null;
    }
    
    private boolean canAnyPieceReachSquare(Player player, Square targetSquare, Square[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square square = board[row][col];
                Piece piece = square.getPiece();
                if (piece != null && piece.getPlayer() == player) {
                    if (isValidMove(square, targetSquare, board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        Piece piece = from.getPiece();
        if (piece == null) return false;
        
        // Basic move validation
        if (!piece.isValidMove(from, to, board)) {
            return false;
        }
        
        // Cannot capture own piece
        if (to.getPiece() != null && to.getPiece().getPlayer() == piece.getPlayer()) {
            return false;
        }
        
        return true;
    }
    
    private Player getOpponent(Player player) {
        return player == Player.WHITE ? Player.BLACK : Player.WHITE;
    }
}
