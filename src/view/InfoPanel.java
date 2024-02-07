package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import javax.swing.JLabel;
import javax.swing.JPanel;
import model.Board;
import model.TetrisBoard;

/**
 * The InfoPanel class that is used to create a panel containing
 * the info for playing the Tetris game.
 *
 * @author jkp117
 * @version 1.2
 */
public final class InfoPanel extends JPanel implements PropertyChangeListener {
    /**
     * Path for the high score folder.
     */
    private static final String HIGH_SCORE_FOLDER = "src/assets/highscore.txt";
    /**
     * Used for the next level info.
     */
    private static final int DIVISOR = 5;
    /**
     * Number of rows for the grid layout.
     */
    private static final int MYGRIDROWS = 12;
    /**
     * Number of columns for the grid layout.
     */
    private static final int MYGRIDCOLUMNS = 1;
    /**
     * Score for level 1
     */
    private static final int MYLEVELSTAGE1 = 40;
    /**
     * Score for level 2
     */
    private static final int MYLEVELSTAGE2 = 100;
    /**
     * Score for level 3
     */
    private static final int MYLEVELSTAGE3 = 300;
    /**
     * Score for level 4
     */
    private static final int MYLEVELSTAGE4 = 1200;
    /**
     * For when theNumRowsCleared = 1
     */
    private static final int MYROWSCLEARED1 = 1;
    /**
     * For when theNumRowsCleared = 2
     */
    private static final int MYROWSCLEARED2 = 2;
    /**
     * For when theNumRowsCleared = 3
     */
    private static final int MYROWSCLEARED3 = 3;
    /**
     * For when theNumRowsCleared = 4
     */
    private static final int MYROWSCLEARED4 = 4;
    /**
     * Amount of rows to progress the levels
     */
    private static final int MYLEVEL = 5;
    /**
     * Size for X of the panel
     */
    private static final int MYWIDTH = 150;
    /**
     * Size for Y of the panel
     */
    private static final int MYHEIGHT = 250;
    /**
     * Origin Score for the game.
     */
    private static final int MYORIGIN = -8;
    /**
     * Array of colors for background of panel.
     */
    private Color[] myTheme;
    /**
     * Represents the player's current score.
     */
    private int myScore = MYORIGIN;
    /**
     * Represents the number n of rows cleared during the game.
     */
    private int myRowsCleared;
    /**
     * The game's current level.
     * Level is the number of rows cleared divided by 5 and then add 1
     */
    private int myLevel = 1;
    /**
     * A label displaying the row count.
     */
    private JLabel myRowsLabel;
    /**
     * A label displaying the score.
     */
    private JLabel myScoreLabel;
    /**
     * A label displaying the current level.
     */
    private JLabel myLevelLabel;
    /**
     * A label displaying the current high score.
     */
    private JLabel myHighScoreLabel;
    /**
     * A label displaying when the next level occurs.
     */
    private JLabel myNextLevelLabel;

    /**
     * A map that stores and puts information regarding the board panel into it.
     */
    private Map<String, Consumer<PropertyChangeEvent>> myMappings;
    /**
     * The constructor sets up the size, color, and layout of the Info Panel.
     */
    public InfoPanel() {
        super();
        setPanel();
        final Board board = TetrisBoard.getInstance();
        board.addPropertyChangeListener(this);
        buildComponents();
        setLayout(new GridLayout(MYGRIDROWS, MYGRIDCOLUMNS));
        layoutComponents();
        myTheme = TetrisPanel.DEFAULT_THEME;
        setUpMappings();
    }
    private int getHighscore() {
        final Scanner s;
        final File file = new File(HIGH_SCORE_FOLDER);
        try {
            s = new Scanner(file);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        int out = 0;
        if (s.hasNextInt()) {
            out = s.nextInt();
        }
        return out;
    }
    private void newHighscore() {
        final File file = new File(HIGH_SCORE_FOLDER);
        final PrintStream p;
        try {
            p = new PrintStream(file);
        } catch (final FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        if (myScore > getHighscore()) {
            p.print(myScore);
        }
    }

    private void setUpMappings() {
        final int pointPerTetrinom = 4;

        myMappings = new HashMap<>();

        myMappings.put(Board.PROPERTY_ROW_CLEARED, e ->
                updateLevel((int) e.getNewValue()));
        myMappings.put(TetrisPanel.PROPERTY_END_GAME, e -> {
            newHighscore();
            displayScore();
            myScore = MYORIGIN;
        });
        myMappings.put(Board.PROPERTY_GAME_OVER, e -> {
            newHighscore();
            displayScore();
            myScore = MYORIGIN;
        });
        myMappings.put(Board.PROPERTY_NEW_GAME, e ->
                resetStats());
        myMappings.put(Board.PROPERTY_NEXT_PIECE, e -> {
            myScore += pointPerTetrinom;
            displayScore();
        });
        myMappings.put(TetrisPanel.PROPERTY_COLOR_CHANGE, e -> {
            myTheme = (Color[]) e.getNewValue();
            repaint();
        });
    }
    private void resetStats() {
        myScore = 0;
        myRowsCleared = 0;
        myLevel = 1;
        displayScore();
    }
    private void updateLevel(final int theNumRowsCleared) {
        myRowsCleared += theNumRowsCleared;
        myLevel = this.myRowsCleared / MYLEVEL + 1;
        if (theNumRowsCleared == MYROWSCLEARED1) {
            myScore += MYLEVELSTAGE1 * myLevel;
        } else if (theNumRowsCleared == MYROWSCLEARED2) {
            myScore += MYLEVELSTAGE2 * myLevel;
        } else if (theNumRowsCleared == MYROWSCLEARED3) {
            myScore += MYLEVELSTAGE3 * myLevel;
        } else if (theNumRowsCleared == MYROWSCLEARED4) {
            myScore += MYLEVELSTAGE4 * myLevel;
        }
        // Still need to change the score and level.
        displayScore();
    }

    /**
     * Sets up the background color and the size of the info panel.
     */
    private void setPanel() {
        setBackground(Color.GREEN);
        setPreferredSize(new Dimension(MYWIDTH, MYHEIGHT));
    }

    /**
     * Displays the current game's score.
     */
    private void displayScore() {
        final int nextLevel = 5 - myRowsCleared % DIVISOR;
        myRowsLabel.setText("Rows Cleared: " + myRowsCleared);
        myLevelLabel.setText("Level: " + myLevel);
        myScoreLabel.setText("Score: " + myScore);
        myHighScoreLabel.setText("High Score: " + getHighscore());
        myNextLevelLabel.setText("Next level in " + nextLevel + " lines");
    }

    private void buildComponents() {

        myRowsLabel = new JLabel("Rows Cleared: 0");
        myLevelLabel = new JLabel("Level: 1");
        myScoreLabel = new JLabel("Score: 0");
        myHighScoreLabel = new JLabel("High Score:");
        myNextLevelLabel = new JLabel("Next level in 5 lines");
    }

    private void layoutComponents() {

        add(myScoreLabel);
        add(myHighScoreLabel);
        add(myRowsLabel);
        add(myLevelLabel);
        add(myNextLevelLabel);

        final JLabel left = new JLabel("Move left: left or A/a");

        final JLabel right = new JLabel("Move right: right or D/d");

        final JLabel down = new JLabel("Move down: down or S/s");

        final JLabel rotate = new JLabel("Rotate CW: up or W/w");

        final JLabel drop = new JLabel("Drop: space");

        final JLabel pause = new JLabel("Pause: P/p");

        final JLabel mute = new JLabel("Mute: M/m");

        paintLabel(List.of(left, right, down, rotate, drop, pause, mute));

        add(left);
        add(right);
        add(down);
        add(rotate);
        add(drop);
        add(pause);
        add(mute);
    }
    private void paintLabel(final List<JLabel> theLabels) {
        for (final JLabel label: theLabels) {
            label.setForeground(Color.WHITE);
        }
    }
    @Override
    protected void paintComponent(final Graphics theGraphics) {
        super.paintComponent(theGraphics);
        final Graphics2D g2d = (Graphics2D) theGraphics;
        // for better graphics display
        fontColor();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        final GradientPaint gp = new GradientPaint(MYWIDTH / 2F, 0,
                myTheme[0],
                MYWIDTH / 2F, MYHEIGHT,
                myTheme[1]);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, MYWIDTH, MYHEIGHT);
        g2d.setPaint(null);
    }

    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (myMappings.containsKey(theEvent.getPropertyName())) {
            myMappings.get(theEvent.getPropertyName()).accept(theEvent);
        }
    }
    private void fontColor() {
        myRowsLabel.setForeground(Color.WHITE);
        myLevelLabel.setForeground(Color.WHITE);
        myScoreLabel.setForeground(Color.WHITE);
        myHighScoreLabel.setForeground(Color.WHITE);
        myNextLevelLabel.setForeground(Color.WHITE);
    }
}
