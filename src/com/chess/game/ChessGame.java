package com.chess.game;

import com.chess.board.ChessBoard;
import com.chess.pieces.Piece;
import com.chess.players.Player;
import com.chess.utils.GameState;
import com.chess.patterns.Observer;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ChessGame implements Observer {
    private static ChessGame instance;
    private GameState currentState;
    private ChessBoard board;
    private Player currentPlayer;
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel rightPanel;
    private List<Piece> whiteCapturedPieces = new ArrayList<>();
    private List<Piece> blackCapturedPieces = new ArrayList<>();

    // Simplified color palette
    private static final Color PRIMARY_COLOR = new Color(240, 240, 240);
    private static final Color ACCENT_COLOR = new Color(200, 220, 240);
    private static final Color TEXT_COLOR = new Color(50, 50, 50);
    private static final Color BORDER_COLOR = new Color(150, 150, 150);

    private ChessGame() {
        try {
            // Set custom look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Customize button UI
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", TEXT_COLOR);
            UIManager.put("Button.font", new Font("Segoe UI", Font.BOLD, 14));
        } catch (Exception e) {
            e.printStackTrace();
        }

        mainFrame = new JFrame("Chess Game");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());

        // Create top panel for buttons
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        topPanel.setBackground(PRIMARY_COLOR);
        
        // Create New Game button
        JButton newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newGameButton.setBackground(ACCENT_COLOR);
        newGameButton.setForeground(TEXT_COLOR);
        newGameButton.setFocusPainted(false);
        newGameButton.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
        newGameButton.addActionListener(e -> resetGame());
        topPanel.add(newGameButton);

        mainFrame.add(topPanel, BorderLayout.NORTH);

        // Create main panel with gradient background
        mainPanel = new JPanel(new BorderLayout(25, 25)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 
                    w, h, new Color(225, 230, 240));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create side panels for captured pieces
        createSidePanels();

        // Create the chess board
        board = new ChessBoard();
        board.addObserver(this);

        // Create center panel with gradient background
        JPanel centerPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR, 
                    w, h, new Color(240, 244, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Add player panels
        centerPanel.add(createPlayerPanel(Player.BLACK), BorderLayout.NORTH);
        centerPanel.add(createBoardPanel(), BorderLayout.CENTER);
        centerPanel.add(createPlayerPanel(Player.WHITE), BorderLayout.SOUTH);

        // Add components to main panel
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        // Create bottom status panel
        createStatusPanel();

        mainFrame.add(mainPanel, BorderLayout.CENTER);
        currentState = GameState.IN_PROGRESS;
        currentPlayer = Player.WHITE;
        mainFrame.pack();
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    public static ChessGame getInstance() {
        if (instance == null) {
            instance = new ChessGame();
        }
        return instance;
    }

    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR,
                    w, h, new Color(236, 240, 248));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Create a sophisticated border effect
        boardPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
            )
        ));

        boardPanel.add(board, BorderLayout.CENTER);
        return boardPanel;
    }

    private JPanel createPlayerPanel(Player player) {
        JPanel panel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR,
                    w, h, new Color(240, 244, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Sophisticated border with shadow effect
        panel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)
            )
        ));

        // Player name and icon panel with gradient
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR,
                    w, h, new Color(240, 244, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };

        // Add crown icon with glow effect for current player
        JLabel turnIndicator = new JLabel("♔");
        turnIndicator.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 24));
        Color playerColor = currentPlayer == player ? PRIMARY_COLOR : BORDER_COLOR;
        turnIndicator.setForeground(playerColor);
        if (currentPlayer == player) {
            turnIndicator.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
        }
        namePanel.add(turnIndicator);

        // Player name with custom styling
        String playerName = player.toString() + " Player";
        JLabel nameLabel = new JLabel("<html><span style='font-family: \"Segoe UI\"; font-size: 16px;'>" +
            playerName + "</span></html>");
        nameLabel.setForeground(TEXT_COLOR);
        namePanel.add(nameLabel);

        // Add move counter with custom styling
        JLabel infoLabel = new JLabel("<html><span style='font-family: \"Segoe UI\"; font-size: 14px;'>" +
            "Moves: 0</span></html>");
        infoLabel.setForeground(TEXT_COLOR);

        panel.add(namePanel, BorderLayout.WEST);
        panel.add(infoLabel, BorderLayout.EAST);

        return panel;
    }

    private void createSidePanels() {
        // Left panel for White's captured pieces
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(PRIMARY_COLOR);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        leftPanel.setPreferredSize(new Dimension(280, 600));

        JLabel whiteLabel = new JLabel("White's Captured Pieces", SwingConstants.CENTER);
        whiteLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        whiteLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        leftPanel.add(whiteLabel);
        leftPanel.add(Box.createVerticalStrut(10));

        // Right panel for Black's captured pieces
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setBackground(PRIMARY_COLOR);
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        rightPanel.setPreferredSize(new Dimension(280, 600));

        JLabel blackLabel = new JLabel("Black's Captured Pieces", SwingConstants.CENTER);
        blackLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        blackLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(blackLabel);
        rightPanel.add(Box.createVerticalStrut(10));
    }

    private JLabel createPanelTitle(String text) {
        JLabel label = new JLabel("<html><div style='text-align: center; width: 100%;'>" +
            "<span style='font-family: \"Segoe UI\"; font-size: 16px; font-weight: bold; color: " + 
            String.format("#%02x%02x%02x", PRIMARY_COLOR.getRed(), 
                PRIMARY_COLOR.getGreen(), PRIMARY_COLOR.getBlue()) + ";'>" +
            text + "</span></div></html>");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 2, 0, PRIMARY_COLOR),
            BorderFactory.createEmptyBorder(0, 0, 8, 0)
        ));
        return label;
    }

    private void createStatusPanel() {
        JPanel statusPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR,
                    w, h, new Color(240, 244, 250));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
            new SoftBevelBorder(SoftBevelBorder.RAISED),
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 20, 12, 20)
            )
        ));

        statusLabel = new JLabel("White's turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statusLabel.setForeground(TEXT_COLOR);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusPanel.add(statusLabel);

        mainPanel.add(statusPanel, BorderLayout.SOUTH);
    }

    private void updateCapturedPiecesPanel(JPanel panel, List<Piece> capturedPieces) {
        // Save the first two components (title and spacing)
        Component title = panel.getComponent(0);
        Component spacing = panel.getComponent(1);
        
        panel.removeAll();
        
        // Restore the title and spacing
        panel.add(title);
        panel.add(spacing);

        // Create a panel for each row of pieces
        JPanel currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        currentRow.setBackground(PRIMARY_COLOR);
        int piecesInRow = 0;

        for (Piece piece : capturedPieces) {
            JPanel pieceContainer = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    int w = getWidth();
                    int h = getHeight();
                    GradientPaint gp = new GradientPaint(0, 0, PRIMARY_COLOR,
                        w, h, new Color(240, 244, 250));
                    g2d.setPaint(gp);
                    g2d.fillRect(0, 0, w, h);
                }
            };
            pieceContainer.setBackground(PRIMARY_COLOR);
            pieceContainer.setPreferredSize(new Dimension(60, 60)); // Make it the same size as board squares
            pieceContainer.setLayout(new BorderLayout());

            // Create a label for the piece image
            JLabel pieceLabel = new JLabel(new ImageIcon(piece.getImage()));
            pieceLabel.setHorizontalAlignment(SwingConstants.CENTER);
            pieceContainer.add(pieceLabel, BorderLayout.CENTER);

            currentRow.add(pieceContainer);
            piecesInRow++;

            if (piecesInRow == 4) { // Show 4 pieces per row
                panel.add(currentRow);
                currentRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
                currentRow.setBackground(PRIMARY_COLOR);
                piecesInRow = 0;
            }
        }

        // Add the last row if it's not empty
        if (piecesInRow > 0) {
            panel.add(currentRow);
        }

        panel.revalidate();
        panel.repaint();
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Player.WHITE) ? Player.BLACK : Player.WHITE;
        updateStatus();
    }

    private void updateStatus() {
        String status = "<html><div style='text-align: center;'>";
        if (currentState == GameState.CHECK) {
            status += "<span style='color: #ff6b6b;'>Check!</span><br>";
        } else if (currentState == GameState.WHITE_WIN) {
            status += "<span style='color: #4cd137;'>Checkmate! White wins!</span>";
            showGameOverDialog("Congratulations! White wins by checkmate! ");
            return;
        } else if (currentState == GameState.BLACK_WIN) {
            status += "<span style='color: #4cd137;'>Checkmate! Black wins!</span>";
            showGameOverDialog("Congratulations! Black wins by checkmate! ");
            return;
        } else if (currentState == GameState.STALEMATE) {
            status += "<span style='color: #ffd32a;'>Game Over - Stalemate!</span>";
            showGameOverDialog("Game Over - It's a draw! (Stalemate)");
            return;
        }
        status += currentPlayer + "'s turn</div></html>";
        statusLabel.setText(status);
    }

    private void showGameOverDialog(String message) {
        SwingUtilities.invokeLater(() -> {
            // Create custom dialog
            JDialog dialog = new JDialog(mainFrame, "Game Over!", true);
            dialog.setLayout(new BorderLayout(10, 10));
            dialog.getContentPane().setBackground(PRIMARY_COLOR);

            // Create main panel with padding
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBackground(PRIMARY_COLOR);
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

            // Add trophy icon for wins
            if (message.contains("wins")) {
                JLabel trophyLabel = new JLabel("", SwingConstants.CENTER);
                trophyLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
                trophyLabel.setForeground(new Color(255, 215, 0));
                trophyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
                panel.add(trophyLabel);
                panel.add(Box.createVerticalStrut(10));
            }

            // Add message
            JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" +
                    "<span style='color: #4cd137; font-size: 16px;'>" + message + "</span></div></html>");
            messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(messageLabel);
            panel.add(Box.createVerticalStrut(20));

            // Add buttons panel
            JPanel buttonsPanel = new JPanel();
            buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.X_AXIS));
            buttonsPanel.setBackground(PRIMARY_COLOR);

            // New Game button
            JButton newGameButton = new JButton("New Game");
            newGameButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            newGameButton.setFont(new Font("Arial", Font.BOLD, 14));
            newGameButton.setBackground(ACCENT_COLOR);
            newGameButton.setForeground(TEXT_COLOR);
            newGameButton.setFocusPainted(false);
            newGameButton.addActionListener(_ -> {
                dialog.dispose();
                resetGame();
            });

            // Exit button
            JButton exitButton = new JButton("Exit");
            exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
            exitButton.setFont(new Font("Arial", Font.BOLD, 14));
            exitButton.setBackground(new Color(255, 107, 107));
            exitButton.setForeground(TEXT_COLOR);
            exitButton.setFocusPainted(false);
            exitButton.addActionListener(_ -> {
                dialog.dispose();
                mainFrame.dispose();
                System.exit(0);
            });

            // Add buttons with spacing
            buttonsPanel.add(Box.createHorizontalGlue());
            buttonsPanel.add(newGameButton);
            buttonsPanel.add(Box.createHorizontalStrut(10));
            buttonsPanel.add(exitButton);
            buttonsPanel.add(Box.createHorizontalGlue());

            panel.add(buttonsPanel);

            dialog.add(panel);
            dialog.pack();
            dialog.setLocationRelativeTo(mainFrame);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialog.setVisible(true);
        });
    }

    public void addCapturedPiece(Piece piece) {
        if (piece.getPlayer() == Player.WHITE) {
            blackCapturedPieces.add(piece);
            updateCapturedPiecesPanel(rightPanel, blackCapturedPieces);
        } else {
            whiteCapturedPieces.add(piece);
            updateCapturedPiecesPanel(leftPanel, whiteCapturedPieces);
        }
    }

    private void resetGame() {
        // Reset game state
        currentPlayer = Player.WHITE;
        currentState = GameState.IN_PROGRESS;
        whiteCapturedPieces.clear();
        blackCapturedPieces.clear();

        // Reset the board
        board.resetBoard();
        
        // Update UI
        updateCapturedPiecesPanel(leftPanel, whiteCapturedPieces);
        updateCapturedPiecesPanel(rightPanel, blackCapturedPieces);
        updateStatus();
        
        // Repaint everything
        mainFrame.repaint();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public GameState getCurrentState() {
        return currentState;
    }

    public void setGameState(GameState state) {
        this.currentState = state;
        updateStatus();
    }

    @Override
    public void update() {
        updateStatus();
    }

    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            ChessGame.getInstance();
        });
    }
}
