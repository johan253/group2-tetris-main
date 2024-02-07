package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import model.Board;
import model.TetrisBoard;

/**
 * This panel displays all the required panels in a frame for the game of Tetris.
 *
 * @author johannn
 * @version 2.2.0
 */
public class TetrisPanel extends JPanel implements PropertyChangeListener {
    /**
     * Holds the default theme colors.
     */
    public static final Color[] DEFAULT_THEME =
            new Color[]{Color.BLACK, Color.BLUE.brighter()};
    /**
     * Holds the nome corresponding to the property change when a game is manually ended.
     */
    public static final String PROPERTY_END_GAME = "end";
    /**
     * Holds the name corresponding to the property change when a theme color changes.
     */
    public static final String PROPERTY_COLOR_CHANGE = "color";
    /**
     * Holds the name corresponding to the property change when the game is paused.
     */
    public static final String PROPERTY_PAUSE = "paused";
    /**
     * Holds the name correspinding to the property change when the grid lines are toggled.
     */
    public static final String PROPERTY_TOGGLE_GRID = "grid";
    /**
     * How many rows cleared at once needed for a Tetris
     */
    private static final int ROWS_NEEDED_FOR_TETRIS = 4;
    /**
     * Holds the color purple for the Huskies theme
     */
    private static final Color PURPLE = new Color(120, 30, 200);
    /**
     * Holds the color gold for the Huskies theme
     */
    private static final Color GOLD = new Color(205, 165, 85);
    /**
     * Holds the file path for the Menu music
     */
    private static final String MENU_MUSIC = "src/assets/menu.wav";
    /**
     * Holds the file path for the music that plays while Playing a game
     */
    private static final String PLAYING_MUSIC = "src/assets/playing.wav";
    /**
     * Holds the file path for the sound when a row is cleared
     */
    private static final String ROW_CLEAR_SOUND = "src/assets/row-clear.wav";
    /**
     * Holds the file path for the sound when 4 rows are cleared
     */
    private static final String TETRIS_CLEAR_SOUND = "src/assets/tetris-clear.wav";
    /**
     * Default starting Tick speed
     */
    private static final int TIMER_TICK = 1000;
    /**
     * Default rate at which the timer decreases the delay every 5 rows cleared
     * (currently set to increase speed my 15% every 5 rows cleared)
     */
    private static final double DIFFICULTY_INCREASE = 85.0 / 100.0;
    /**
     * This field sets the gap between all the panels and the window of this panel
     */
    private static final int PANEL_GAP = 2;
    /**
     * The panel holding the sub-panels on the right side of the screen
     */
    private JPanel myRightSide;
    /**
     * The Board that hold the game of Tetris
     */
    private final Board myBoard;
    /**
     * The timer to determine when the board should advance
     */
    private final Timer myTimer;
    /**
     * The number of rows cleared so far in the current game
     */
    private int myRowsCleared;
    /**
     * The Property change support to inform listeners of changes
     */
    private final PropertyChangeSupport myPCS;
    /**
     * The Clip object used to play the music in the background
     */
    private final Clip myClip;
    /**
     * The button in the Menu to end an ongoing game
     */
    private JMenuItem myEndGameButton;
    /**
     * The button in the Menu to start a new game if there isn't one ongoing
     */
    private JMenuItem myNewGameButton;

    /**
     * This constructor makes and displays all of the components needed for
     * the Tetris game.
     */
    public TetrisPanel() {
        super();
        myBoard = TetrisBoard.getInstance();
        myTimer = new Timer(TIMER_TICK, e -> myBoard.step());
        myBoard.addPropertyChangeListener(this);
        myRowsCleared = 0;
        myPCS = new PropertyChangeSupport(this);
        try {
            myClip = AudioSystem.getClip();
        } catch (final LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        setUpLayout();
        setUpComponents();
    }

    /**
     * This method plays a music file continuously, stopping any music currently
     * playing on the myClip Object.
     *
     * @param theMusic the String filepath of the music to play
     */
    private void playMusic(final String theMusic) {
        final File song = new File(theMusic);
        final AudioInputStream songStream;
        try {
            songStream = AudioSystem.getAudioInputStream(song);
        } catch (final UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            myClip.stop();
            myClip.close();
            myClip.open(songStream);
            myClip.start();
            myClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (final LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Plays a sound once with no looping.
     *
     * @param theSound the sound to be played
     */
    private void playSound(final String theSound) {
        final File song = new File(theSound);
        final AudioInputStream songStream;
        try {
            songStream = AudioSystem.getAudioInputStream(song);
        } catch (final UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
        try {
            final Clip clip = AudioSystem.getClip();
            clip.open(songStream);
            clip.start();
        } catch (final LineUnavailableException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * This set up method sets up the main panel and layout of the game
     */
    private void setUpLayout() {
        final BorderLayout mainPanelLayout = new BorderLayout(PANEL_GAP, PANEL_GAP);
        setLayout(mainPanelLayout);

        setBorder(new EmptyBorder(PANEL_GAP, PANEL_GAP, PANEL_GAP, PANEL_GAP));

        final BorderLayout rightSideBorderLayout = new BorderLayout();
        rightSideBorderLayout.setVgap(PANEL_GAP);
        myRightSide = new JPanel(rightSideBorderLayout);
    }

    /**
     * This set up method sets up the components that are going to be used in this frame/panel.
     */
    private void setUpComponents() {
        final BoardPanel board = new BoardPanel(myBoard);
        myPCS.addPropertyChangeListener(board);

        final NextPiecePanel nextPiece = new NextPiecePanel(myBoard);
        myPCS.addPropertyChangeListener(nextPiece);

        final InfoPanel info = new InfoPanel();
        myPCS.addPropertyChangeListener(info);

        myRightSide.add(nextPiece, BorderLayout.NORTH);
        myRightSide.add(info, BorderLayout.CENTER);

        setFocusable(true);
        requestFocus();
        addKeyListener(new ControlKeyListener());

        add(board, BorderLayout.CENTER);
        add(myRightSide, BorderLayout.WEST);
        playMusic(MENU_MUSIC);
    }

    /**
     * Handles what happens when you click the new game button
     *
     * @param theEvent the Action event from pressing a button
     */
    private void handleNewGameClick(final ActionEvent theEvent) {
        myBoard.newGame();
        myTimer.start();
        myEndGameButton.setEnabled(true);
        myNewGameButton.setEnabled(false);
        playMusic(PLAYING_MUSIC);
    }

    /**
     * Handles what happens when you click the About button.
     *
     * @param theEvent the Action Event dispatched from a button
     */
    private void handleAboutClick(final ActionEvent theEvent) {
        JOptionPane.showMessageDialog(null,
                """
                        Made by TCSS 305 Group 2:

                        Johan Hernandez,
                        Ian Salsich,
                        Duy-Hung Cong Le,
                        Jayden Kitiona Peneueta
                        
                        for comments/feedback, please email johannjo2000@gmail.com

                        Autumn 2023, rights not reserved ©
                        """,
                "Info", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Handles what happens when you click the end game button.
     *
     * @param theEvent the Action Event from the button that was clicked
     */
    private void handleEndGameClick(final ActionEvent theEvent) {
        myEndGameButton.setEnabled(false);
        myNewGameButton.setEnabled(true);
        myTimer.stop();
        myTimer.setDelay(TIMER_TICK);
        myRowsCleared = 0;
        playMusic(MENU_MUSIC);
        myPCS.firePropertyChange(PROPERTY_END_GAME, null, null);
    }

    /**
     * This method creates and adds the JMenuBar for the Tetris GUI.
     *
     * @param theFrame is the frame this is being added to
     * @return the JMenuBar for the frame
     */
    private JMenuBar createMenuBar(final JFrame theFrame) {
        final JMenuBar menuBar = new JMenuBar();

        final JMenu gameMenu = getGameMenu(theFrame);

        final JMenu otherMenu = getOtherMenu();

        menuBar.add(gameMenu);
        menuBar.add(otherMenu);

        return menuBar;
    }

    /**
     * Gets the Game Menu that is shown with all the JMenuItems, Action Listeners, and
     * Mnemonics set up.
     *
     * @param theFrame used to make the exit button work
     * @return the JMenu Game Menu shown
     */
    private JMenu getGameMenu(final JFrame theFrame) {
        final JMenu out = new JMenu("Game");
        out.setMnemonic(KeyEvent.VK_G);

        createGameManagementButtons();

        final JMenuItem exitGameButton = new JMenuItem("Exit");
        exitGameButton.addActionListener(e ->
                theFrame.dispatchEvent(new WindowEvent(theFrame, WindowEvent.WINDOW_CLOSING)));

        out.add(myNewGameButton);
        out.add(myEndGameButton);
        out.add(exitGameButton);
        return out;
    }

    /**
     * Gets the Other Menu with all the JMenuItems, Action Listeners, and
     * Mnemonics set up.
     *
     * @return the JMenu Other Menu shown
     */
    private JMenu getOtherMenu() {
        final JMenu out = new JMenu("Other");
        out.setMnemonic(KeyEvent.VK_O);

        final JMenuItem aboutButton =  new JMenuItem("About");
        aboutButton.setMnemonic(KeyEvent.VK_A);
        aboutButton.addActionListener(this::handleAboutClick);

        final JMenuItem toggleGrid = new JMenuItem("Toggle Grid Lines");
        toggleGrid.setMnemonic(KeyEvent.VK_L);
        toggleGrid.addActionListener(e -> myPCS.firePropertyChange
                (PROPERTY_TOGGLE_GRID, null, null));

        final JMenu colorChange = getThemeMenu();

        out.add(aboutButton);
        out.add(toggleGrid);
        out.add(colorChange);

        return out;
    }
    /**
     * Gets the Theme Menu with all the JMenuItems, Action Listeners, and
     * Mnemonics set up.
     *
     * @return the JMenu Theme Menu shown
     */
    private JMenu getThemeMenu() {
        //testing
        final JMenu colorChange = new JMenu("Select Theme");
        colorChange.setMnemonic(KeyEvent.VK_S);
        final JMenuItem defaultTheme = new JMenuItem("Default");
        defaultTheme.addActionListener(e -> myPCS.firePropertyChange(
                PROPERTY_COLOR_CHANGE, null, DEFAULT_THEME
        ));
        final JMenuItem darkTheme = new JMenuItem("Dark");
        darkTheme.addActionListener(e -> myPCS.firePropertyChange(
                PROPERTY_COLOR_CHANGE, null, new Color[]{Color.BLACK, Color.DARK_GRAY}
        ));
        final JMenuItem lightTheme = new JMenuItem("Light");
        lightTheme.addActionListener(e -> myPCS.firePropertyChange(
                PROPERTY_COLOR_CHANGE, null, new Color[]{Color.GRAY, Color.WHITE}
        ));
        final JMenuItem huskyTheme = new JMenuItem("Husky");
        huskyTheme.addActionListener(e -> myPCS.firePropertyChange(
                PROPERTY_COLOR_CHANGE, null, new Color[]{PURPLE, GOLD}
        ));
        colorChange.add(defaultTheme);
        colorChange.add(darkTheme);
        colorChange.add(lightTheme);
        colorChange.add(huskyTheme);

        return colorChange;
    }

    /**
     * Creates the buttons responsible for starting and ending a new game with
     * the Action Listeners and Mnemonics
     */
    private void createGameManagementButtons() {
        myNewGameButton = new JMenuItem("New Game");
        myNewGameButton.setMnemonic(KeyEvent.VK_N);

        myEndGameButton = new JMenuItem("End Game");
        myEndGameButton.setMnemonic(KeyEvent.VK_E);
        myEndGameButton.setEnabled(false);

        myNewGameButton.addActionListener(this::handleNewGameClick);
        myEndGameButton.addActionListener(this::handleEndGameClick);
    }

    /**
     * Create the Tetris GUI and display it. For thread safety,
     * this method should be invoked from the event-dispatching
     * thread.
     */
    public static void createAndShowGUI() {
        final JFrame frame = new JFrame("Tetris");
        final ImageIcon logo = new ImageIcon("src/assets/logo.png");
        frame.setIconImage(logo.getImage());

        final TetrisPanel contentPane = new TetrisPanel();
        contentPane.setOpaque(true);
        frame.setContentPane(contentPane);
        frame.setJMenuBar(contentPane.createMenuBar(frame));

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    /**
     * Handles property change events from the Board model.
     *
     * @param theEvent A PropertyChangeEvent object describing the event source
     *          and the property that has changed.
     */
    @Override
    public void propertyChange(final PropertyChangeEvent theEvent) {
        if (theEvent.getPropertyName().equals(myBoard.PROPERTY_GAME_OVER)) {
            myRowsCleared = 0;
            myTimer.stop();
            myTimer.setDelay(TIMER_TICK);
            myEndGameButton.setEnabled(false);
            myNewGameButton.setEnabled(true);
            playMusic(MENU_MUSIC);
        } else if (theEvent.getPropertyName().equals(myBoard.PROPERTY_ROW_CLEARED)) {
            updateTimer((int) theEvent.getNewValue());
        }
    }

    /**
     * Updates the tick speed of the Swing Timer depending on how many
     * rows have been cleared thus so far. (Increases speed of Timer by 15%
     * every 5 rows cleared.
     *
     * @param theNumClearedRows the number of rows cleared in the last row clear event
     */
    private void updateTimer(final int theNumClearedRows) {
        myRowsCleared += theNumClearedRows;
        final int level = myRowsCleared / 5 + 1;
        double tick = TIMER_TICK;
        for (int i = 1; i < level; i++) {
            tick *= DIFFICULTY_INCREASE;
        }
        if (theNumClearedRows >= ROWS_NEEDED_FOR_TETRIS) {
            playSound(TETRIS_CLEAR_SOUND);
        } else {
            playSound(ROW_CLEAR_SOUND);
        }
        myTimer.setDelay((int) tick);
    }

    /**
     * Handles pausing the music
     */
    private void toggleMusicMute() {
        if (myClip.isRunning()) {
            myClip.stop();
        } else {
            myClip.start();
        }
    }

    /**
     * Private inner class that handles key events for the Tetris Game
     *
     * @author johannn
     * @author duyhung
     * @version 1.0.0
     */
    private final class ControlKeyListener extends KeyAdapter {
        /**
         * The map that holds the Runnable corresponding to each KeyEvent trigger
         */
        private Map<Integer, Runnable> myKeyMaps;

        /**
         * Constructor instantiates all fields and calls super()
         */
        ControlKeyListener() {
            super();
            mapTheKeys();
        }

        /**
         * Maps the appropriate KeyEvent to its corresponding Runnable
         * to set up what each key event hould do.
         */
        private void mapTheKeys() {
            myKeyMaps = new HashMap<>();
            myKeyMaps.put(KeyEvent.VK_W, myBoard::rotateCW);
            myKeyMaps.put(KeyEvent.VK_UP, myBoard::rotateCW);
            myKeyMaps.put(KeyEvent.VK_A, myBoard::left);
            myKeyMaps.put(KeyEvent.VK_LEFT, myBoard::left);
            myKeyMaps.put(KeyEvent.VK_S, myBoard::down);
            myKeyMaps.put(KeyEvent.VK_DOWN, myBoard::down);
            myKeyMaps.put(KeyEvent.VK_D, myBoard::right);
            myKeyMaps.put(KeyEvent.VK_RIGHT, myBoard::right);
            myKeyMaps.put(KeyEvent.VK_SPACE, myBoard::drop);

            myKeyMaps.put(KeyEvent.VK_M, TetrisPanel.this::toggleMusicMute);
        }
        /**
         * Handles key press events to move the current piece on the Tetris board.
         *
         * @param theEvent the event to be processed
         */
        @Override
        public void keyPressed(final KeyEvent theEvent) {
            if (myTimer.isRunning() && myKeyMaps.containsKey(theEvent.getKeyCode())) {
                myKeyMaps.get(theEvent.getKeyCode()).run();
            }
            if (theEvent.getKeyCode() == KeyEvent.VK_P
                && myEndGameButton.isEnabled()) {
                if (myTimer.isRunning()) {
                    myTimer.stop();
                } else {
                    myTimer.start();
                }
                myPCS.firePropertyChange(PROPERTY_PAUSE, null, null);
            }
        }

    }
}

