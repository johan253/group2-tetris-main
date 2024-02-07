package edu.uw.tcss.app;

import view.TetrisPanel;

/**
 * Main class to run and display the Tetris App.
 *
 * @author johannn
 * @version 1.0
 */
public final class Main {

    private Main() {
        super();
    }

    /**
     * The main method of execution for this class.
     *
     * @param theArgs is the command line arguments
     */
    public static void main(final String[] theArgs) {
        javax.swing.SwingUtilities.invokeLater(TetrisPanel::createAndShowGUI);
    }
}
