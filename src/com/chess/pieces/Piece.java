package com.chess.pieces;

import com.chess.board.Square;
import com.chess.patterns.MovementStrategy;
import com.chess.players.Player;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class Piece {
    protected Player player;
    protected BufferedImage image;
    protected MovementStrategy movementStrategy;
    
    public Piece(Player player, String imageName) {
        this.player = player;
        try {
            // Use class loader to load images from resources
            String imagePath = "d:/chess_game/src/images/" + imageName;
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                System.err.println("Image file not found: " + imagePath);
                // Try alternate path
                imagePath = "src/images/" + imageName;
                imageFile = new File(imagePath);
                if (!imageFile.exists()) {
                    System.err.println("Image file not found in alternate path: " + imagePath);
                }
            }
            this.image = ImageIO.read(imageFile);
        } catch (IOException e) {
            System.err.println("Error loading image: " + imageName);
            e.printStackTrace();
        }
    }
    
    public void draw(Graphics g, int x, int y, int size) {
        if (image != null) {
            g.drawImage(image, x, y, size, size, null);
        } else {
            // Fallback drawing if image fails to load
            g.setColor(player == Player.WHITE ? Color.WHITE : Color.BLACK);
            g.fillOval(x + 10, y + 10, size - 20, size - 20);
            g.setColor(player == Player.WHITE ? Color.BLACK : Color.WHITE);
            g.drawOval(x + 10, y + 10, size - 20, size - 20);
            // Draw piece type letter
            g.setFont(new Font("Arial", Font.BOLD, size / 2));
            g.drawString(getName().substring(0, 1), x + size/3, y + 2*size/3);
        }
    }
    
    public Player getPlayer() {
        return player;
    }
    
    public boolean isValidMove(Square from, Square to, Square[][] board) {
        return movementStrategy.isValidMove(from, to, board);
    }
    
    public BufferedImage getImage() {
        return image;
    }
    
    public ImageIcon getImageIcon() {
        return new ImageIcon(getImage());
    }
    
    public abstract String getName();
}
