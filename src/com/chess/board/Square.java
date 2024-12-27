package com.chess.board;

import com.chess.pieces.Piece;
import java.awt.*;

public class Square {
    private final int row;
    private final int col;
    private final boolean isLight;
    private Piece piece;
    private boolean isSelected;
    
    public Square(int row, int col, boolean isLight) {
        this.row = row;
        this.col = col;
        this.isLight = isLight;
        this.piece = null;
        this.isSelected = false;
    }
    
    public void draw(Graphics g, int x, int y, int size) {
        Graphics2D g2d = (Graphics2D) g;
        
        // Draw square background
        g2d.setColor(isLight ? ChessBoard.LIGHT_SQUARE : ChessBoard.DARK_SQUARE);
        if (isSelected) {
            g2d.setColor(ChessBoard.SELECTED_SQUARE);
        }
        g2d.fillRect(x, y, size, size);
        
        // Add subtle border
        g2d.setColor(isLight ? ChessBoard.DARK_SQUARE.darker() : ChessBoard.LIGHT_SQUARE.darker());
        g2d.setStroke(new BasicStroke(1.0f));
        g2d.drawRect(x, y, size, size);
        
        // Draw piece with shadow effect if present
        if (piece != null) {
            // Draw shadow
            g2d.setColor(new Color(0, 0, 0, 30));
            piece.draw(g2d, x + 2, y + 2, size);
            
            // Draw actual piece
            piece.draw(g2d, x, y, size);
        }
    }
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    public Piece getPiece() { return piece; }
    public void setPiece(Piece piece) { this.piece = piece; }
    public void setSelected(boolean selected) { this.isSelected = selected; }
}
