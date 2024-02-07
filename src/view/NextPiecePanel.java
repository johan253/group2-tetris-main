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
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import model.Block;
import model.Board;
import model.Rotation;
import model.TetrisPiece;




/**
 * This is the NextPiecePanel class that creates the panel for the Tetris game.
 * @author Duy-Hung
 * @version 1.3
 */
public final class NextPiecePanel extends JPanel implements PropertyChangeListener {
    /**
     * Font for the information
     */
    private static final int INFOFONT = 9;
    /**
     * Font for the title
     */
    private static final int  TITLEFONT = 20;
    /**
     * Font for the seperation of info rows
     */
    private static final int SEPERATE = 20;
    /**
     * Size for X of the panel
     */
    private static final int MYWIDTH = 150;
    /**
     * Size for Y of the panel
     */
    private static final int MYHEIGHT = 150;
    /**
     * Distance of 1 square
     */
    private static final int MYSIZE = 5;
    /**
     * Board of the Tetris game
     */
    private final Board myBoard;
    /**
     * The next piece of Tetris game
     */
    private TetrisPiece myPiece;
    /**
     * The colors for the tetris pieces
     */
    private Map<Block, Color> myBlockColors;
    /**
     * Map of the specific rotations of the shapes
     */
    private Map<TetrisPiece, Rotation> myTetrisPieceMap = new HashMap<>();
    /**
     * Array of colors for background of panel.
     */
    private Color[] myTheme;
    /**
     * If the game is over or not
     */
    private boolean myGameOver;
    /**
     * The next piece constructor.
     * @param theBoard is the board that is passed from the tetris main panel.
     */
    public NextPiecePanel(final Board theBoard) {
        super();
        setTetrisPieceRotation();
        setUpColors();
        myTheme = TetrisPanel.DEFAULT_THEME;
        myBoard = theBoard;
        myBoard.addPropertyChangeListener(this);
        this.setPreferredSize(new Dimension(MYWIDTH, MYHEIGHT));
    }
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(myBoard.PROPERTY_NEXT_PIECE)) {
            myPiece = (TetrisPiece) theEvent.getNewValue();
            repaint();
        } else if (theEvent.getPropertyName().equals(TetrisPanel.PROPERTY_COLOR_CHANGE)) {
            myTheme = (Color[]) theEvent.getNewValue();
            repaint();
        } else if (theEvent.getPropertyName().equals(myBoard.PROPERTY_GAME_OVER)) {
            myGameOver = (Boolean) theEvent.getNewValue();
            repaint();
        }
    }
    /**
     * Paints some ellipses.
     *
     * @param theGraphics The graphics context to use for painting.
     */
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        // for better graphics display
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        paintGameInfoMenu(g2d);
        paintBackgroundInGame(g2d);
        if (myPiece != null && !myGameOver) {
            paintBackgroundInGame(g2d);
            paintTetrisPiece(g2d);
        } else if (myGameOver) {
            paintGameInfoMenu(g2d);
            myGameOver = false;
        } else {
            paintGameInfoMenu(g2d);
        }
    }
    private void paintTetrisPiece(final Graphics2D theG2d) {
        int[][] rotation = new int[1][];
        int tetrisPieceIAdjustmentsX = 0;
        int tetrisPieceIAdjustmentsY = 0;
        if (myPiece.equals(TetrisPiece.O)) {
            tetrisPieceIAdjustmentsX = MYSIZE + MYSIZE + MYSIZE;
            tetrisPieceIAdjustmentsY = MYSIZE + MYSIZE + MYSIZE;
        } else if (myPiece.equals(TetrisPiece.I)) {
            tetrisPieceIAdjustmentsX = MYSIZE + MYSIZE + MYSIZE;
            tetrisPieceIAdjustmentsY = MYSIZE + MYSIZE + MYSIZE + MYSIZE + MYSIZE;
        } else if (myPiece.equals(TetrisPiece.T)
                || myPiece.equals(TetrisPiece.L)
                || myPiece.equals(TetrisPiece.J)) {
            tetrisPieceIAdjustmentsY = -(MYSIZE + MYSIZE + MYSIZE);
        } else if (myPiece.equals(TetrisPiece.S)
                || myPiece.equals(TetrisPiece.Z)) {
            tetrisPieceIAdjustmentsY = MYSIZE + MYSIZE + MYSIZE;
        }
        if (myTetrisPieceMap.containsKey(myPiece)) {
            rotation = myPiece.getPointsByRotation(myTetrisPieceMap.get(myPiece));
        }
        nextPieceHelper(theG2d, tetrisPieceIAdjustmentsX, tetrisPieceIAdjustmentsY, rotation);
    }
    private void nextPieceHelper(final Graphics2D theG2d, final int theX,
                                 final int theY, final int[][] theRotation) {
        theG2d.setStroke(new BasicStroke(2));
        for (final int[] ints : theRotation) {
            theG2d.setColor(myBlockColors.get(myPiece.getBlock()));
            theG2d.fillRect(MYWIDTH / MYSIZE + ints[1]
                            * MYWIDTH / MYSIZE - theX,
                    MYHEIGHT / MYSIZE + ints[0]
                            * MYHEIGHT / MYSIZE - theY,
                    MYWIDTH / MYSIZE, MYHEIGHT / MYSIZE);
            theG2d.setPaint(Color.BLACK);
            theG2d.drawRect(MYWIDTH / MYSIZE + ints[1]
                            * MYWIDTH / MYSIZE - theX,
                    MYHEIGHT / MYSIZE + ints[0]
                            * MYHEIGHT / MYSIZE - theY,
                    MYWIDTH / MYSIZE, MYHEIGHT / MYSIZE);
        }
    }
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
    private void setTetrisPieceRotation() {
        myTetrisPieceMap = new HashMap<>();
        myTetrisPieceMap.put(TetrisPiece.T, Rotation.THREEQUARTER);
        myTetrisPieceMap.put(TetrisPiece.J, Rotation.THREEQUARTER);
        myTetrisPieceMap.put(TetrisPiece.L, Rotation.THREEQUARTER);
        myTetrisPieceMap.put(TetrisPiece.Z, Rotation.QUARTER);
        myTetrisPieceMap.put(TetrisPiece.S, Rotation.QUARTER);
        myTetrisPieceMap.put(TetrisPiece.I, Rotation.QUARTER);
        myTetrisPieceMap.put(TetrisPiece.O, Rotation.NONE);
    }
    private void paintBackgroundInGame(final Graphics2D theG2d) {
        final GradientPaint gp = new GradientPaint(MYWIDTH / 2F, 0,
                myTheme[0],
                MYWIDTH / 2F, MYHEIGHT,
                myTheme[1]);
        theG2d.setPaint(gp);
        theG2d.fillRect(0, 0, MYWIDTH, MYHEIGHT);
        theG2d.setPaint(null);
    }
    private void paintGameInfoMenu(final Graphics2D theG2d) {
        final String title = "Scoring";
        final String linesCleared = "       line 1  line 2  line 3  line 4";
        final String pointsLV1 =    "L1   40      100      300      1.2k ";
        final String pointsLV2 =    "L2   80      200      600      24.k ";
        final String pointsLV3 =    "L3   120     300      900      3.6k ";
        final String pointsLV10 =   "L10  400     1k       3k        12k ";
        theG2d.setColor(Color.WHITE);
        theG2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, TITLEFONT));
        final FontMetrics fontMetrics = theG2d.getFontMetrics();
        final int centeredX = (MYWIDTH - fontMetrics.stringWidth(title)) / 2;
        final int centeredY = (MYHEIGHT - fontMetrics.getHeight()) / 8
                + fontMetrics.getHeight() / 8;
        final int yCordForInfo = 150;
        final int xCordForInfo = (MYWIDTH - fontMetrics.stringWidth(title)) / 8;
        theG2d.drawString(title, centeredX, centeredY);
        theG2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, INFOFONT));
        //Seperates the rows of information by 20;
        theG2d.drawString(pointsLV1, xCordForInfo, yCordForInfo - SEPERATE * 2);
        theG2d.drawString(pointsLV2, xCordForInfo, yCordForInfo - SEPERATE * (2 + 1));
        theG2d.drawString(pointsLV3, xCordForInfo, yCordForInfo - SEPERATE * (2 + 2));
        theG2d.drawString(pointsLV10, xCordForInfo, yCordForInfo - SEPERATE * (2 + 2 + 1));
        theG2d.drawString(linesCleared, xCordForInfo, yCordForInfo - SEPERATE * (2 + 2 + 2));
    }
}
