package edu.uw.tcss.app;

import java.util.logging.Level;
import java.util.logging.Logger;
import model.TetrisBoard;

/**
 * This Class is used for testing purposes.
 *
 * @author johannn
 * @author Duy-Hung
 * @author jkp117
 * @author ianpsal
 * @version 1.0
 */

public final class SandBox {
    /**
     * Logger Object for more robust logging
     */
    private static final Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    /**
     * New Line Character
     */
    private static final String NEW_LINE = "\n";
    private SandBox() {
        super();
    }

    /**
     * Driver method for this class.
     *
     * @param theArgs command line arguments
     */
    public static void main(final String[] theArgs) {
        final TetrisBoard b = TetrisBoard.getInstance();
        b.newGame();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);

        b.step();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);
        b.rotateCW();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);
        b.rotateCW();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);
        b.rotateCW();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);
        b.rotateCW();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);
        b.drop();
        LOGGER.log(Level.INFO, NEW_LINE + b);
//        System.out.println(b);

    }

}
