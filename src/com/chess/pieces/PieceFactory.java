package com.chess.pieces;

import com.chess.players.Player;

public class PieceFactory {
    private static PieceFactory instance = new PieceFactory();
    
    private PieceFactory() {}
    
    public static PieceFactory getInstance() {
        return instance;
    }
    
    public Piece createPiece(String type, Player player) {
        switch(type.toLowerCase()) {
            case "pawn":
                return new Pawn(player);
            case "rook":
                return new Rook(player);
            case "knight":
                return new Knight(player);
            case "bishop":
                return new Bishop(player);
            case "queen":
                return new Queen(player);
            case "king":
                return new King(player);
            default:
                throw new IllegalArgumentException("Unknown piece type: " + type);
        }
    }
}
