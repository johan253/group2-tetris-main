package model;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * This interface represent a tetris board.
 * @author johannn
 * @author Duy-Hung
 * @author jkp117
 * @author ianpsal
 * @version 1.3
 */
public interface Board {
    /**
     * Name for property change for game over.
     */
    String PROPERTY_GAME_OVER = "Game over";
    /**
     * Name for the property change for when the next piece changes.
     */
    String PROPERTY_NEXT_PIECE = "Next piece";
    /**
     *
     */
    String PROPERTY_CURRENT_PIECE = "Current piece";
    /**
     * Name for the property change for when the board changes.
     */
    String PROPERTY_BOARD_CHANGE = "Board change";
    /**
     * Name for the property change for when a row is cleared.
     */
    String PROPERTY_ROW_CLEARED = "Row cleared";
    /**
     * Name for the property change for when a new game is started.
     */
    String PROPERTY_NEW_GAME = "New game";

    /**
     * Get the width of the board.
     *
     * @return Width of the board.
     */
    int getWidth();

    /**
     * Get the height of the board.
     *
     * @return Height of the board.
     */
    int getHeight();



    /**
     * Resets the board for a new game.
     * This method must be called before the first game
     * and before each new game.
     */
    void newGame();

    /**
     * Sets a non random sequence of pieces to loop through.
     *
     * @param thePieces the List of non random TetrisPieces.
     */
    void setPieceSequence(List<TetrisPiece> thePieces);

    /**
     * Advances the board by one 'step'.
     * <p>
     * This could include
     * - moving the current piece down 1 line
     * - freezing the current piece if appropriate
     * - clearing full lines as needed
     */
    void step();

    /**
     * Try to move the movable piece down.
     * Freeze the Piece in position if down tries to move into an illegal state.
     * Clear full lines.
     */
    void down();

    /**
     * Try to move the movable piece left.
     */
    void left();

    /**
     * Try to move the movable piece right.
     */
    void right();

    /**
     * Try to rotate the movable piece in the clockwise direction.
     */
    void rotateCW();

    /**
     * Try to rotate the movable piece in the counter-clockwise direction.
     */
    void rotateCCW();

    /**
     * Drop the piece until piece is set.
     */
    void drop();

    /**
     * Add a property change listener.
     *
     * @param theListener the listener being added
     */
    void addPropertyChangeListener(PropertyChangeListener theListener);

    /**
     * Add a property change listener to a specific property.
     *
     * @param theListener the listener being added
     * @param thePropertyName the property being added
     */
    void addPropertyChangeListener(PropertyChangeListener theListener,
                                   String thePropertyName);

    /**
     * Remove a property change listener.
     *
     * @param theListener the listener object
     */
    void removePropertyChangeListener(PropertyChangeListener theListener);

    /**
     * Remove a property change listener from a specific property.
     *
     * @param theListener the listener object
     * @param thePropertyName the name of the property
     */
    void removePropertyChangeListener(PropertyChangeListener theListener,
                                      String thePropertyName);
}
