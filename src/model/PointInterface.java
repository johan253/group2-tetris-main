package model;

/**
 * This interface represent a tetris piece.
 * @author johannn
 * @author Duy-Hung
 * @author jkp117
 * @author ianpsal
 * @version 1.3
 */
public interface PointInterface {

    /**
     * Provides the current x coordinate of the piece.
     *
     * @return The current x coordinate.
     */
    int x();

    /**
     * Provides the current y coordinate of the piece.
     *
     * @return The current y coordinate.
     */
    int y();

    /**
     * Creates a new Point object that is transformed via the
     * provided x and y coordinates.
     *
     * @param theX the X factor to transform by.
     * @param theY the Y factor to transform by.
     * @return the new transformed Point.
     */
    Point transform(int theX, int theY);

    /**
     * Creates a new Point object from another Point object.
     *
     * @param thePoint the Point to transform with.
     * @return the new transformed Point.
     */
    Point transform(Point thePoint);
}
