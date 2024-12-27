# Chess Game Implementation

## Technical Documentation

### Design Patterns Used

1. **Singleton Pattern**
   - Used in `ChessGame` class to ensure only one game instance exists
   - Also used in `PieceFactory` for centralized piece creation

2. **Factory Pattern**
   - Implemented through `PieceFactory` class
   - Centralizes piece creation and allows for easy piece type modifications

3. **Observer Pattern**
   - Used for board state updates
   - Implemented through `Subject` and `Observer` interfaces
   - Allows for loose coupling between board state and UI updates

4. **Strategy Pattern**
   - Used for piece movement rules
   - Each piece type has its own movement strategy
   - Allows for easy modification of movement rules

### Class Structure

- `ChessGame`: Main game controller (Singleton)
- `ChessBoard`: Manages the game board and piece placement
- `Square`: Represents a single board square
- `Piece`: Abstract base class for chess pieces
- `MovementStrategy`: Interface for piece movement rules
- Concrete piece classes: `Pawn`, `Rook`, `Knight`, `Bishop`, `Queen`, `King`

### Key Features

1. Complete chess rules implementation
2. Graphical user interface
3. Turn-based gameplay
4. Move validation
5. Game state management
6. Visual feedback

### Dependencies

- Java Swing for GUI
- Java SE 8 or higher

### Building and Running

1. Compile all Java files:
   ```bash
   javac com/chess/game/*.java
   ```

2. Run the game:
   ```bash
   java com.chess.game.ChessGame
   ```

### Future Extensions

The modular design allows for easy addition of:
- AI opponents
- Network play
- Move history
- Game saving/loading
- Different board themes
"# Chess-game-JAVA" 
