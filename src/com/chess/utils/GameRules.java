package com.chess.utils;

import com.chess.board.Square;
import com.chess.pieces.*;
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
        // First find the king's position
        Square kingSquare = findKing(player, board);
        if (kingSquare == null) return false;
        
        // Check if any opponent's piece can attack the king
        Player opponent = getOpponent(player);
        return canAnyPieceReachSquare(opponent, kingSquare, board);
    }
    
    public boolean isCheckmate(Player player, Square[][] board) {
        // First check if the king is in check
        if (!isKingInCheck(player, board)) {
            return false;
        }
        
        // Try every possible move for every piece
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Square fromSquare = board[fromRow][fromCol];
                if (fromSquare.getPiece() != null && fromSquare.getPiece().getPlayer() == player) {
                    // Try moving this piece to every possible square
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Square toSquare = board[toRow][toCol];
                            // Check if this move is valid and would get out of check
                            if (isValidMove(fromSquare, toSquare, board) && 
                                !wouldMoveLeaveKingInCheck(fromSquare, toSquare, board)) {
                                return false; // Found at least one legal move
                            }
                        }
                    }
                }
            }
        }
        
        // If we get here, no legal moves were found
        return true;
    }
    
    public boolean isStalemate(Player player, Square[][] board) {
        // First verify the king is NOT in check
        if (isKingInCheck(player, board)) {
            return false;
        }
        
        // Check if there are any legal moves
        return !hasAnyValidMove(player, board);
    }
    
    public boolean wouldMoveLeaveKingInCheck(Square from, Square to, Square[][] board) {
        if (from.getPiece() == null) return false;
        
        // Save current state
        Piece originalFromPiece = from.getPiece();
        Piece originalToPiece = to.getPiece();
        
        // Make temporary move
        to.setPiece(from.getPiece());
        from.setPiece(null);
        
        // Check if king would be in check after this move
        boolean kingInCheck = isKingInCheck(originalFromPiece.getPlayer(), board);
        
        // Restore original position
        from.setPiece(originalFromPiece);
        to.setPiece(originalToPiece);
        
        return kingInCheck;
    }
    
    private boolean hasAnyValidMove(Player player, Square[][] board) {
        for (int fromRow = 0; fromRow < 8; fromRow++) {
            for (int fromCol = 0; fromCol < 8; fromCol++) {
                Square fromSquare = board[fromRow][fromCol];
                if (fromSquare.getPiece() != null && fromSquare.getPiece().getPlayer() == player) {
                    for (int toRow = 0; toRow < 8; toRow++) {
                        for (int toCol = 0; toCol < 8; toCol++) {
                            Square toSquare = board[toRow][toCol];
                            if (isValidMove(fromSquare, toSquare, board) && 
                                !wouldMoveLeaveKingInCheck(fromSquare, toSquare, board)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
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
    
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        Piece piece = from.getPiece();
        if (piece == null) return false;
        
        // Check if move is within board bounds
        if (to.getRow() < 0 || to.getRow() >= 8 || to.getCol() < 0 || to.getCol() >= 8) {
            return false;
        }
        
        // Check if destination has a piece of the same color
        if (to.getPiece() != null && to.getPiece().getPlayer() == piece.getPlayer()) {
            return false;
        }
        
        // Check piece-specific movement rules
        return piece.isValidMove(from, to, board);
    }
    
    private Player getOpponent(Player player) {
        return player == Player.WHITE ? Player.BLACK : Player.WHITE;
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
}
