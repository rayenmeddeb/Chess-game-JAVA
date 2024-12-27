package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

public class Queen extends Piece {
    public Queen(Player player) {
        super(player, player == Player.WHITE ? "WQueen.png" : "BQueen.png");
        this.movementStrategy = new QueenMovementStrategy();
    }
    
    @Override
    public String getName() {
        return "Queen";
    }
}

class QueenMovementStrategy implements MovementStrategy {
    private final RookMovementStrategy rookStrategy = new RookMovementStrategy();
    private final BishopMovementStrategy bishopStrategy = new BishopMovementStrategy();
    
    @Override
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        return rookStrategy.isValidMove(from, to, board) || 
               bishopStrategy.isValidMove(from, to, board);
    }
}
