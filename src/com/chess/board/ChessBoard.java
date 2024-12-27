package com.chess.board;

import com.chess.game.ChessGame;
import com.chess.patterns.Observer;
import com.chess.patterns.Subject;
import com.chess.pieces.*;
import com.chess.players.Player;
import com.chess.utils.GameRules;
import com.chess.utils.GameState;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class ChessBoard extends JPanel implements Subject {
    private static final int BOARD_SIZE = 8;
    private static final int SQUARE_SIZE = 80;
    public static final Color LIGHT_SQUARE = new Color(238, 238, 210);
    public static final Color DARK_SQUARE = new Color(118, 150, 86);
    public static final Color SELECTED_SQUARE = new Color(186, 202, 68);
    private static final Color BOARD_BORDER = new Color(76, 76, 76);
    private static final Color OVERLAY_BG = new Color(0, 0, 0, 180);
    private static final Color WIN_TEXT_COLOR = new Color(255, 215, 0);

    private Square[][] squares;
    private Square selectedSquare;
    private List<Observer> observers;
    private GameRules gameRules;
    private PieceFactory pieceFactory;
    private GameState currentGameState = GameState.IN_PROGRESS;

    public ChessBoard() {
        observers = new ArrayList<>();
        gameRules = GameRules.getInstance();
        pieceFactory = PieceFactory.getInstance();

        int boardPixelSize = BOARD_SIZE * SQUARE_SIZE;
        setPreferredSize(new Dimension(boardPixelSize + 40, boardPixelSize + 40));
        setLayout(null); // Using null layout for custom positioning
        setBorder(new LineBorder(BOARD_BORDER, 20));
        setBackground(BOARD_BORDER);

        initializeBoard();
        setupPieces();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // Adjust for border
                int adjustedX = e.getX() - 20;
                int adjustedY = e.getY() - 20;

                if (adjustedX >= 0 && adjustedX < boardPixelSize &&
                    adjustedY >= 0 && adjustedY < boardPixelSize) {
                    int row = adjustedY / SQUARE_SIZE;
                    int col = adjustedX / SQUARE_SIZE;
                    handleSquareClick(row, col);
                }
            }
        });
    }

    private void initializeBoard() {
        squares = new Square[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col] = new Square(row, col, (row + col) % 2 == 0);
            }
        }
    }

    private void setupPieces() {
        // Setup black pieces
        placePiece(pieceFactory.createPiece("rook", Player.BLACK), 0, 0);
        placePiece(pieceFactory.createPiece("knight", Player.BLACK), 0, 1);
        placePiece(pieceFactory.createPiece("bishop", Player.BLACK), 0, 2);
        placePiece(pieceFactory.createPiece("queen", Player.BLACK), 0, 3);
        placePiece(pieceFactory.createPiece("king", Player.BLACK), 0, 4);
        placePiece(pieceFactory.createPiece("bishop", Player.BLACK), 0, 5);
        placePiece(pieceFactory.createPiece("knight", Player.BLACK), 0, 6);
        placePiece(pieceFactory.createPiece("rook", Player.BLACK), 0, 7);

        for (int col = 0; col < BOARD_SIZE; col++) {
            placePiece(pieceFactory.createPiece("pawn", Player.BLACK), 1, col);
        }

        // Setup white pieces
        placePiece(pieceFactory.createPiece("rook", Player.WHITE), 7, 0);
        placePiece(pieceFactory.createPiece("knight", Player.WHITE), 7, 1);
        placePiece(pieceFactory.createPiece("bishop", Player.WHITE), 7, 2);
        placePiece(pieceFactory.createPiece("queen", Player.WHITE), 7, 3);
        placePiece(pieceFactory.createPiece("king", Player.WHITE), 7, 4);
        placePiece(pieceFactory.createPiece("bishop", Player.WHITE), 7, 5);
        placePiece(pieceFactory.createPiece("knight", Player.WHITE), 7, 6);
        placePiece(pieceFactory.createPiece("rook", Player.WHITE), 7, 7);

        for (int col = 0; col < BOARD_SIZE; col++) {
            placePiece(pieceFactory.createPiece("pawn", Player.WHITE), 6, col);
        }
    }

    private void placePiece(Piece piece, int row, int col) {
        squares[row][col].setPiece(piece);
    }

    private void handleSquareClick(int row, int col) {
        Square clickedSquare = squares[row][col];
        ChessGame game = ChessGame.getInstance();
        Player currentPlayer = game.getCurrentPlayer();

        // Don't allow moves if game is over
        if (currentGameState == GameState.WHITE_WIN || 
            currentGameState == GameState.BLACK_WIN || 
            currentGameState == GameState.STALEMATE) {
            return;
        }

        if (selectedSquare == null) {
            if (clickedSquare.getPiece() != null && 
                clickedSquare.getPiece().getPlayer() == currentPlayer) {
                selectedSquare = clickedSquare;
                selectedSquare.setSelected(true);
            }
        } else {
            if (gameRules.isValidMove(selectedSquare, clickedSquare, squares)) {
                if (!gameRules.wouldMoveLeaveKingInCheck(selectedSquare, clickedSquare, squares)) {
                    // Make the move
                    movePiece(selectedSquare, clickedSquare);

                    // Switch to next player
                    Player opponent = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;

                    // Check game state after move
                    if (gameRules.isCheckmate(opponent, squares)) {
                        // Current player wins by checkmate
                        currentGameState = (currentPlayer == Player.WHITE) ? GameState.WHITE_WIN : GameState.BLACK_WIN;
                        game.setGameState(currentGameState);
                    } else if (gameRules.isStalemate(opponent, squares)) {
                        currentGameState = GameState.STALEMATE;
                        game.setGameState(currentGameState);
                    } else if (gameRules.isKingInCheck(opponent, squares)) {
                        currentGameState = GameState.CHECK;
                        game.setGameState(currentGameState);
                    } else {
                        currentGameState = GameState.IN_PROGRESS;
                        game.setGameState(currentGameState);
                    }

                    game.switchPlayer();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Invalid move: Would leave your king in check!", 
                        "Invalid Move", 
                        JOptionPane.WARNING_MESSAGE);
                }
            }
            selectedSquare.setSelected(false);
            selectedSquare = null;
        }
        repaint();
        notifyObservers();
    }

    private void movePiece(Square from, Square to) {
        // Check if there's a piece being captured
        if (to.getPiece() != null) {
            ChessGame.getInstance().addCapturedPiece(to.getPiece());
        }
        
        // Move the piece
        to.setPiece(from.getPiece());
        from.setPiece(null);
    }

    public void resetBoard() {
        // Clear all squares
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col].setPiece(null);
            }
        }
        
        // Initialize the board with new pieces
        initializeBoard();
        setupPieces();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Enable antialiasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Draw coordinates
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 14));

        // Draw column coordinates (a-h)
        for (int col = 0; col < BOARD_SIZE; col++) {
            String letter = String.valueOf((char)('a' + col));
            g2d.drawString(letter, 20 + col * SQUARE_SIZE + SQUARE_SIZE/2 - 5, BOARD_SIZE * SQUARE_SIZE + 35);
        }

        // Draw row coordinates (1-8)
        for (int row = 0; row < BOARD_SIZE; row++) {
            String number = String.valueOf(8 - row);
            g2d.drawString(number, 5, 20 + row * SQUARE_SIZE + SQUARE_SIZE/2 + 5);
        }

        // Draw the board and pieces
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                squares[row][col].draw(g2d, 20 + col * SQUARE_SIZE, 20 + row * SQUARE_SIZE, SQUARE_SIZE);
            }
        }

        // Draw game over overlay if game is finished
        if (currentGameState == GameState.WHITE_WIN || 
            currentGameState == GameState.BLACK_WIN || 
            currentGameState == GameState.STALEMATE) {
            drawGameOverOverlay(g2d);
        }
    }

    private void drawGameOverOverlay(Graphics2D g2d) {
        int boardWidth = BOARD_SIZE * SQUARE_SIZE + 40;  // Including border
        int boardHeight = BOARD_SIZE * SQUARE_SIZE + 40;

        // Draw semi-transparent overlay
        g2d.setColor(OVERLAY_BG);
        g2d.fillRect(0, 0, boardWidth, boardHeight);

        // Prepare the message
        String message;
        if (currentGameState == GameState.WHITE_WIN) {
            message = "CHECKMATE!\nWhite Wins!";
        } else if (currentGameState == GameState.BLACK_WIN) {
            message = "CHECKMATE!\nBlack Wins!";
        } else {
            message = "STALEMATE!\nGame Drawn";
        }

        // Draw message background
        int messageX = boardWidth / 2;
        int messageY = boardHeight / 2;
        Font titleFont = new Font("Arial", Font.BOLD, 36);
        Font subtitleFont = new Font("Arial", Font.BOLD, 24);

        String[] lines = message.split("\n");

        // Draw the main title
        g2d.setFont(titleFont);
        FontMetrics fmTitle = g2d.getFontMetrics();
        Rectangle2D titleBounds = fmTitle.getStringBounds(lines[0], g2d);

        // Draw the subtitle
        g2d.setFont(subtitleFont);
        FontMetrics fmSubtitle = g2d.getFontMetrics();
        Rectangle2D subtitleBounds = fmSubtitle.getStringBounds(lines[1], g2d);

        // Calculate total height
        double totalHeight = titleBounds.getHeight() + subtitleBounds.getHeight() + 10; // 10px spacing

        // Draw background rectangle
        double maxWidth = Math.max(titleBounds.getWidth(), subtitleBounds.getWidth());
        int padding = 30;
        g2d.setColor(new Color(40, 40, 40, 230));
        g2d.fillRoundRect(
            (int)(messageX - maxWidth/2 - padding),
            (int)(messageY - totalHeight/2 - padding),
            (int)(maxWidth + padding*2),
            (int)(totalHeight + padding*2),
            20, 20
        );

        // Draw the text
        g2d.setColor(WIN_TEXT_COLOR);

        // Draw title
        g2d.setFont(titleFont);
        g2d.drawString(lines[0], 
            (int)(messageX - titleBounds.getWidth()/2),
            (int)(messageY - totalHeight/2 + titleBounds.getHeight())
        );

        // Draw subtitle
        g2d.setFont(subtitleFont);
        g2d.drawString(lines[1],
            (int)(messageX - subtitleBounds.getWidth()/2),
            (int)(messageY - totalHeight/2 + titleBounds.getHeight() + 10 + subtitleBounds.getHeight())
        );
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update();
        }
    }

    public Square[][] getSquares() {
        return squares;
    }
}
