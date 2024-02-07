package view;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import javax.swing.JPanel;
import model.Block;
import model.Board;
import model.MyMovableTetrisPiece;
import model.Point;

/**
 * This class holds the board for an ongoing game of Tetris.
 *
 * @author johannn
 * @author ian
 * @author Hung
 * @author jkp117
 * @version 2.2.0
 */
public class BoardPanel extends JPanel implements PropertyChangeListener {
    /**
     * Holds the total width of the board panel
     */
    private static final int WIDTH = 200;
    /**
     * Holds the total height of the board panel
     */
    private static final int HEIGHT = 400;
    /**
     * Holds the unit width of the board in regards to tetrimino blocks
     */
    private static final int GRID_WIDTH = 10;
    /**
     * Holds the unit height of the board in regards to tetrimino blocks
     */
    private static final int GRID_HEIGHT = 20;
    /**
     * Holds the amount of tetrimino blocks that can be held in the board horizontally
     */
    private static final int BLOCK_WIDTH = WIDTH / GRID_WIDTH;
    /**
     * Holds the amount of tetrimino blocks that can be held in the board vertically
     */
    private static final int BLOCK_HEIGHT = HEIGHT / GRID_HEIGHT;
    /**
     * The Font that is used for displaying text in the game
     */
    private static final Font TEXT_FONT = new Font(Font.SANS_SERIF, Font.BOLD, 20);
    /**
     * Stores the current theme
     */
    private Color[] myTheme;
    /**
     * A list containing all the colors for each tetrimino
     */
    private Map<Block, Color> myBlockColors;
    /**
     * Contains the only instance of the board model
     */
    private final Board myBoard;
    /**
     * Contains the current movable piece to display
     */
    private MyMovableTetrisPiece myCurrentPiece;
    /**
     * Contains the board data to display
     */
    private List<Block[]> myBoardData;
    /**
     * Stores whether the current game has been lost / quit
     */
    private boolean myGameOver;
    /**
     * Stores whether the current came is paused or not
     */
    private boolean myPaused;
    /**
     * Stores whether the grid lines should be drawn
     */
    private boolean myGridLines;
    /**
     *
     */
    private final Map<String, Consumer<PropertyChangeEvent>> myPropertyMaps;

    /**
     * Constructor sets up the layout, size, and color of the Board Panel.
     *
     */
    public BoardPanel(final Board theBoard) {
        super();
        myBoard = theBoard;
        myPropertyMaps = new HashMap<>();
        setUpColors();
        setUpPanel();
        setUpPropertyMaps();
    }

    /**
     * Sets up the property change event mappings
     */
    private void setUpPropertyMaps() {
        myPropertyMaps.put(myBoard.PROPERTY_CURRENT_PIECE, e ->
                myCurrentPiece = (MyMovableTetrisPiece) e.getNewValue());
        myPropertyMaps.put(myBoard.PROPERTY_BOARD_CHANGE, e ->
                myBoardData = (List<Block[]>) e.getNewValue());
        myPropertyMaps.put(myBoard.PROPERTY_ROW_CLEARED, e ->
                myCurrentPiece = null);
        myPropertyMaps.put(myBoard.PROPERTY_GAME_OVER, e ->
                myGameOver = true);
        myPropertyMaps.put(myBoard.PROPERTY_NEW_GAME, e ->
                resetBoard());
        myPropertyMaps.put(TetrisPanel.PROPERTY_PAUSE, e -> {
            if (!myGameOver) {
                myPaused = !myPaused;
            }
        });
        myPropertyMaps.put(TetrisPanel.PROPERTY_TOGGLE_GRID, e ->
                myGridLines = !myGridLines);
        myPropertyMaps.put(TetrisPanel.PROPERTY_COLOR_CHANGE, e ->
                myTheme = (Color[]) e.getNewValue());
        myPropertyMaps.put(TetrisPanel.PROPERTY_END_GAME, e -> {
            if (myPaused) {
                myPaused = false;
            }
            myGameOver = true;
        });
    }

    /**
     * This method sets up the panel in which the board is held
     */
    private void setUpPanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
//        setBackground(Color.RED);
        myBoard.addPropertyChangeListener(this);
        myCurrentPiece = null;
        myGridLines = false;
        myGameOver = false;
        myPaused = false;
    }

    /**
     * This method sets up the colors that will be used for each tetrimino
     */
    private void setUpColors() {
        myTheme = TetrisPanel.DEFAULT_THEME;

        myBlockColors = new HashMap<>();
        myBlockColors.put(Block.J, Color.BLUE);
        myBlockColors.put(Block.O, Color.YELLOW);
        myBlockColors.put(Block.I, Color.CYAN);
        myBlockColors.put(Block.L, Color.ORANGE);
        myBlockColors.put(Block.T, Color.MAGENTA);
        myBlockColors.put(Block.Z, Color.RED);
        myBlockColors.put(Block.S, Color.GREEN);

    }

    /**
     * This method paints all seven tetriminos on the baord for now.
     *
     * @param theGraphics the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g = (Graphics2D) theGraphics;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        final GradientPaint gp = new GradientPaint(WIDTH / 2F, 0,
                                                    myTheme[0],
                                                   WIDTH / 2F, HEIGHT,
                                                    myTheme[1]);
        g.setPaint(gp);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        g.setPaint(null);
        if (myCurrentPiece == null
            && myBoardData == null) {
            paintStartScreen(g);
        }
        if (myCurrentPiece != null) {
            paintCurrentPiece(g);
        }
        if (myBoardData != null) {
            paintOngoingBoard(g);
        }
        if (myPaused) {
            paintPauseScreen(g);
        } else if (myGridLines && myBoardData != null) {
            paintGridLines(g);
        }
        if (myGameOver) {
            paintGameOver(g);
        }
    }

    /**
     * paints the grid lines if they are toggled
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintGridLines(final Graphics2D theGraphics) {
        final int alpha = 100;

        theGraphics.setColor(new Color(0, 0, 0, alpha));
        for (int i = 1; i < GRID_WIDTH; i++) {
            theGraphics.drawLine(i * BLOCK_WIDTH, 0, i * BLOCK_WIDTH, HEIGHT);
        }
        for (int i = 1; i < GRID_HEIGHT; i++) {
            theGraphics.drawLine(0, i * BLOCK_HEIGHT, WIDTH, i * BLOCK_HEIGHT);
        }
    }

    /**
     * Paints the starting screen when you open the game
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintStartScreen(final Graphics2D theGraphics) {
        final String text = "TETRIS";

        theGraphics.setColor(Color.DARK_GRAY);
        theGraphics.setFont(new Font(Font.SANS_SERIF, Font.BOLD, TEXT_FONT.getSize() * 2));
        final FontMetrics fontMetrics = theGraphics.getFontMetrics();
        final int centeredX = (WIDTH - fontMetrics.stringWidth(text)) / 2;
        final int centeredY = (HEIGHT - fontMetrics.getHeight()) / 2
                + fontMetrics.getHeight() / 2;
        theGraphics.drawString(text, centeredX, centeredY);

        theGraphics.setColor(Color.WHITE);
        theGraphics.drawString(text, centeredX - 2, centeredY - 2);
    }
    /**
     * Paints the pause screen when you pause a game
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintPauseScreen(final Graphics2D theGraphics) {
        final String text = "PAUSED";
        theGraphics.setColor(Color.BLACK);
        theGraphics.fillRect(0, 0, WIDTH, HEIGHT);
        theGraphics.setColor(Color.WHITE);
        theGraphics.setFont(TEXT_FONT);
        final FontMetrics fontMetrics = theGraphics.getFontMetrics();
        final int centeredX = (WIDTH - fontMetrics.stringWidth(text)) / 2;
        final int centeredY = (HEIGHT - fontMetrics.getHeight()) / 2
                + fontMetrics.getHeight() / 2;
        theGraphics.drawString(text, centeredX, centeredY);
    }
    /**
     * Paints the current piece on the board
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintCurrentPiece(final Graphics2D theGraphics) {

        final Point[] points = myCurrentPiece.getBoardPoints();

        theGraphics.setStroke(new BasicStroke(2));
        for (final Point point : points) {
            theGraphics.setColor(myBlockColors.
                    get(myCurrentPiece.getTetrisPiece().getBlock()));
            theGraphics.fillRect(point.x() * BLOCK_WIDTH,
                    (-point.y() + GRID_HEIGHT - 1) * BLOCK_HEIGHT,
                    BLOCK_WIDTH, BLOCK_HEIGHT);
            theGraphics.setColor(Color.BLACK);
            theGraphics.drawRect(point.x() * BLOCK_WIDTH,
                    (-point.y() + GRID_HEIGHT - 1) * BLOCK_HEIGHT,
                    BLOCK_WIDTH, BLOCK_HEIGHT);
        }
    }
    /**
     * Paints the ongoing board of the game
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintOngoingBoard(final Graphics2D theGraphics) {

        theGraphics.setStroke(new BasicStroke(2));
        for (int i = myBoardData.size() - 1; i >= 0; i--) {
            for (int j = 0; j < myBoardData.get(0).length; j++) {
                final Block cur = myBoardData.get(i)[j];
                if (cur != null) {
                    theGraphics.setColor(myBlockColors.get(cur));
                    theGraphics.fillRect(j * BLOCK_WIDTH,
                            (-i + GRID_HEIGHT - 1) * BLOCK_HEIGHT,
                            BLOCK_WIDTH, BLOCK_HEIGHT);
                    theGraphics.setColor(Color.BLACK);
                    theGraphics.drawRect(j * BLOCK_WIDTH,
                            (-i + GRID_HEIGHT - 1) * BLOCK_HEIGHT,
                            BLOCK_WIDTH, BLOCK_HEIGHT);
                }
            }
        }
    }
    /**
     * Paints the game over screen
     *
     * @param theGraphics the graphics object passed from paintComponent
     */
    private void paintGameOver(final Graphics2D theGraphics) {
        final String text = "Game Over!";
        paintOngoingBoard(theGraphics);

        // Draw black rectangle behind text
        final int boxHeight = HEIGHT / 10;

        theGraphics.setColor(Color.BLACK);
        theGraphics.fillRect(0, HEIGHT / 2 - (boxHeight / 2), WIDTH, boxHeight);

        // Draw text centered on the board
        theGraphics.setColor(Color.WHITE);
        theGraphics.setFont(TEXT_FONT);
        final FontMetrics fontMetrics = theGraphics.getFontMetrics();
        final int centeredX = (WIDTH - fontMetrics.stringWidth(text)) / 2;
        final int centeredY = (HEIGHT - fontMetrics.getHeight()) / 2
                + fontMetrics.getHeight() / 2;
        theGraphics.drawString(text, centeredX, centeredY);
    }

    /**
     * Handles property change events from objects the board is listening to.
     *
     * @param theEvent A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (myPropertyMaps.containsKey(theEvent.getPropertyName())) {
            myPropertyMaps.get(theEvent.getPropertyName()).
                    andThen(e -> repaint()).accept(theEvent);
        }
    }

    /**
     * Resets the fields to restart the look of the game
     */
    private void resetBoard() {
        myPaused = false;
        myGameOver = false;
        myBoardData = new ArrayList<>();
        myCurrentPiece = null;
        repaint();
    }
}
