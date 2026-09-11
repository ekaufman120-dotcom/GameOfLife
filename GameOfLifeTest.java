/**
 * Lightweight tests for GameOfLife.
 *
 * No JUnit setup is required. Run this file like any other Java program.
 * Some tests are EXPECTED TO FAIL until you complete the TODO methods.
 */
public class GameOfLifeTest {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {

        testConstructorAndBasicMethods();
        testNeighborCountInMiddle();
        testNeighborCountAtEdge();
        testNoWrapAround();
        testBlinkerUpdate();

        System.out.println();
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + failed);

        if (failed == 0) {
            System.out.println("All starter tests passed!");
        }
        else {
            System.out.println("Keep going - failing tests show what still needs work.");
        }
    }

    private static void testConstructorAndBasicMethods() {
        GameOfLife game = new GameOfLife(5, 8);

        check("constructor: correct row count",
                game.numberOfRows() == 5);

        check("constructor: correct column count",
                game.numberOfColumns() == 8);

        check("new board starts empty",
                !game.cellAt(2, 3));

        game.growCellAt(2, 3);
        check("growCellAt makes a cell alive",
                game.cellAt(2, 3));

        game.killCellAt(2, 3);
        check("killCellAt makes a cell dead",
                !game.cellAt(2, 3));
    }

    private static void testNeighborCountInMiddle() {
        GameOfLife game = new GameOfLife(5, 5);

        game.growCellAt(1, 1);
        game.growCellAt(1, 2);
        game.growCellAt(2, 1);

        check("neighborCount: middle cell counts 3 neighbors",
                game.neighborCount(2, 2) == 3);
    }

    private static void testNeighborCountAtEdge() {
        GameOfLife game = new GameOfLife(5, 5);

        game.growCellAt(0, 1);
        game.growCellAt(1, 0);
        game.growCellAt(1, 1);

        check("neighborCount: corner counts only in-bounds neighbors",
                game.neighborCount(0, 0) == 3);
    }

    private static void testNoWrapAround() {
        GameOfLife game = new GameOfLife(5, 5);

        // This cell is diagonally opposite (0,0).
        // It must NOT count as a neighbor.
        game.growCellAt(4, 4);

        check("neighborCount: board does not wrap around",
                game.neighborCount(0, 0) == 0);
    }

    private static void testBlinkerUpdate() {
        GameOfLife game = new GameOfLife(5, 5);

        // Horizontal blinker
        game.growCellAt(2, 1);
        game.growCellAt(2, 2);
        game.growCellAt(2, 3);

        game.update();

        boolean correct =
                game.cellAt(1, 2) &&
                game.cellAt(2, 2) &&
                game.cellAt(3, 2) &&
                !game.cellAt(2, 1) &&
                !game.cellAt(2, 3);

        check("update: horizontal blinker becomes vertical",
                correct);
    }

    private static void check(String testName, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("PASS: " + testName);
        }
        else {
            failed++;
            System.out.println("FAIL: " + testName);
        }
    }
}
