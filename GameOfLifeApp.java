import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * A simple GUI + text viewer for the GameOfLife model.
 *
 * Students should not need to modify this file for the core project.
 */
public class GameOfLifeApp extends JFrame {

    private static final int ROWS = 30;
    private static final int COLS = 45;
    private static final int DELAY = 100; // delay in milliseconds

    private GameOfLife game;
    private final JButton[][] cells;
    private GameOfLife previousState;

    private final JButton startButton;
    private final JButton resetButton;
    private final JButton invertButton;
    private final JLabel generationLabel;
    private final JLabel dimensionLabel;
    private final JLabel statusLabel;
    private final JTextArea textView;

    private final Timer timer;
    private int generation;
    private int lastGeneration;

    public GameOfLifeApp() {
        super("Conway's Game of Life - 2D Arrays");

        game = new GameOfLife(ROWS, COLS);
        cells = new JButton[ROWS][COLS];

        startButton = new JButton("Start");
        resetButton = new JButton("Reset");
        invertButton = new JButton("Invert");
        generationLabel = new JLabel("Generation: 0");
        dimensionLabel = new JLabel("Dimensions: " + ROWS + " x " + COLS);
        statusLabel = new JLabel("Click cells to create a pattern, or add a glider.");
        textView = new JTextArea();

        generation = 0;

        timer = new Timer(DELAY, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stepGeneration();
            }
        });

        buildWindow();
        refreshDisplay();
    }

    private void buildWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        add(buildControlPanel(), BorderLayout.NORTH);
        add(buildMainPanel(), BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        setSize(1050, 610);
        setLocationRelativeTo(null);
    }

    private JPanel buildControlPanel() {
        JPanel controls = new JPanel();

        JButton stepButton = new JButton("Step");
        JButton clearButton = new JButton("Clear");
        JButton gliderButton = new JButton("Add Glider");

        stepButton.addActionListener(e -> stepGeneration());
        startButton.addActionListener(e -> toggleAnimation());
        clearButton.addActionListener(e -> clearBoard());
        resetButton.addActionListener(e -> resetBoard());
        gliderButton.addActionListener(e -> addGlider());
        invertButton.addActionListener(e -> invertBoard());
        controls.add(dimensionLabel);
        controls.add(stepButton);
        controls.add(startButton);
        controls.add(clearButton);
        controls.add(resetButton);
        controls.add(gliderButton);
        controls.add(invertButton);
        controls.add(generationLabel);
        return controls;
    }

    private JSplitPane buildMainPanel() {
        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS, 1, 1));
        gridPanel.setBackground(Color.GRAY);
        gridPanel.setBorder(BorderFactory.createTitledBorder("GUI View"));

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(22, 22));
                cellButton.setFocusPainted(false);
                cellButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                cellButton.setOpaque(true);

                final int r = row;
                final int c = col;
                cellButton.addActionListener(e -> toggleCell(r, c));

                cells[row][col] = cellButton;
                gridPanel.add(cellButton);
            }
        }

        textView.setEditable(false);
        textView.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 16));
        textView.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JScrollPane textScroll = new JScrollPane(textView);
        textScroll.setBorder(BorderFactory.createTitledBorder("Text View (toString)"));

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                gridPanel,
                textScroll
        );

        splitPane.setResizeWeight(0.68);
        splitPane.setDividerLocation(700);

        return splitPane;
    }

    private void toggleCell(int row, int col) {
        /* (timer.isRunning()) {
            statusLabel.setText("Stop the animation before editing individual cells.");
            return;
        }*/

        if (game.cellAt(row, col)) {
            game.killCellAt(row, col);
        }
        else {
            game.growCellAt(row, col);
        }

        refreshDisplay();
    }

    private void stepGeneration() {
        if (touchesWall()) {
            stopAnimation();
            statusLabel.setText("A live cell reached the wall. Animation stopped.");
            return;
        }

        game.update();
        generation++;
        refreshDisplay();

        if (touchesWall()) {
            stopAnimation();
            statusLabel.setText("A live cell reached the wall. Animation stopped.");
        }
        else {
            statusLabel.setText("Generation advanced.");
        }
    }

    private void toggleAnimation() {
        if (timer.isRunning()) {
            stopAnimation();
            statusLabel.setText("Animation stopped.");
        }
        else {
            if (touchesWall()) {
                statusLabel.setText("Move the pattern away from the wall before starting.");
                return;
            }

            lastGeneration = generation;
            previousState = new GameOfLife(game.numberOfRows(), game.numberOfColumns());
            for (int row = 0; row < game.numberOfRows(); row++) {
                for (int col = 0; col < game.numberOfColumns(); col++) {
                    if (game.cellAt(row, col)) {
                        previousState.growCellAt(row, col);
                    }
                }
            }
            timer.start();
            startButton.setText("Stop");
            statusLabel.setText("Animation running...");
        }
    }
    
    private void stopAnimation() {
        timer.stop();
        startButton.setText("Start");
    }

    private void clearBoard() {
        stopAnimation();
        game.clear();
        generation = 0;
        refreshDisplay();
        statusLabel.setText("Board cleared.");
    }

    private void resetBoard() {
        stopAnimation();
        game = previousState;
        generation = lastGeneration;
        refreshDisplay();
        statusLabel.setText("Board reset.");
    }

    private void addGlider() {
        // Standard five-cell glider, placed away from the walls.
        int row = 2;
        int col = 2;

        game.growCellAt(row, col + 1);
        game.growCellAt(row + 1, col + 2);
        game.growCellAt(row + 2, col);
        game.growCellAt(row + 2, col + 1);
        game.growCellAt(row + 2, col + 2);

        refreshDisplay();
        statusLabel.setText("Glider added. Complete update() to make it move.");
    }

    private boolean touchesWall() {
        int lastRow = game.numberOfRows() - 1;
        int lastCol = game.numberOfColumns() - 1;
        /*
        for (int col = 0; col < game.numberOfColumns(); col++) {
            if (game.cellAt(0, col) || game.cellAt(lastRow, col)) {
                return true;
            }
        }

        for (int row = 0; row < game.numberOfRows(); row++) {
            if (game.cellAt(row, 0) || game.cellAt(row, lastCol)) {
                return true;
            }
        }*/

        return false;
    }

    private void refreshDisplay() {
        for (int row = 0; row < game.numberOfRows(); row++) {
            for (int col = 0; col < game.numberOfColumns(); col++) {
                if (game.cellAt(row, col)) {
                    cells[row][col].setBackground(Color.BLACK);
                }
                else {
                    cells[row][col].setBackground(Color.WHITE);
                }
            }
        }

        generationLabel.setText("Generation: " + generation);
        textView.setText(game.toString());
        textView.setCaretPosition(0);
    }

    private void invertBoard() {
        for (int row = 0; row < game.numberOfRows(); row++) {
            for (int col = 0; col < game.numberOfColumns(); col++) {
                if (game.cellAt(row, col)) {
                    game.killCellAt(row, col);
                }
                else {
                    game.growCellAt(row, col);
                }
            }
        }
        refreshDisplay();
        statusLabel.setText("Board inverted.");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameOfLifeApp app = new GameOfLifeApp();
            app.setVisible(true);
        });
    }
}
