/**
 * 
 */
/**
 * 
 */
module chess_game {
    requires transitive java.desktop;
    requires java.base;

    exports com.chess.board;
    exports com.chess.game;
    exports com.chess.pieces;
    exports com.chess.players;
    exports com.chess.utils;
    exports com.chess.patterns;
}