package com.thetinkeringtypist.pentastick.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PentastickUsingDoubles implements Puzzle {

    private static final double[] WING_ROW = new double[] {0, 1, 0, 1, 0, 0, 2, 0, 2, 0, 0, 3, 0, 3, 0, 0, 4, 0, 4, 0, 0, 5, 0, 5, 0};
    private static final double[] EDGE_ROW = new double[] {1, 0, 1, 0, 1, 2, 0, 2, 0, 2, 3, 0, 3, 0, 3, 4, 0, 4, 0, 4, 5, 0, 5, 0, 5};
    private static final int NUM_ROWS_IN_SLICE = 7;
    private static final int NUM_COLS_IN_SLICE = 8;
    private static final List<Por> POR_LIST = new ArrayList<>(Por.allOf());

    /**
     * The pieces of pentastick. This starts off as the initial state of the puzzle and is updated with rotations
     */
    private final double[][] pieces;


    /**
     * Constructs a pentastick puzzle whose initial state is the solved state.
     */
    public PentastickUsingDoubles() {
        this(null);
    }

    public PentastickUsingDoubles(final double[][] initialState) {
        pieces = new double[11][25];

        if (Objects.nonNull(initialState)) {
            for (int i = 0; i < initialState.length; i++) {
                System.arraycopy(initialState[i], 0, pieces[i], 0, initialState[i].length);
            }
        } else {
            for (int i = 0; i < pieces.length; i++) {
                if (i % 2 == 0) {
                    // Even rows get wing row
                    System.arraycopy(WING_ROW, 0, pieces[i], 0, WING_ROW.length);
                } else {
                    // Odd rows get edge row
                    System.arraycopy(EDGE_ROW, 0, pieces[i], 0, EDGE_ROW.length);
                }
            }
        }
    }

    @Override
    public String toString() {
        int rowCounter = 0;
        StringBuilder builder = new StringBuilder("    |");
        for (int i = 0; i < pieces[0].length; i++) {
            builder.append(String.format("%3d", i)).append(" ");
        }

        builder.append("\n");
        builder.append(String.format("----+%s\n", "-".repeat(100)));

        for (double[] row : pieces) {
            builder.append(String.format("%3d | ", rowCounter));
            for (double p : row) {
                builder.append(String.format("%2d ", p)).append(" ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append("\n");
            rowCounter++;
        }

        return builder.toString();
    }

    /**
     * Rotates the pieces in the given POR by 180 degrees.
     *
     * @param por the plane of rotation to manipulate.
     */
    @Override
    public void rotate(final Por por) {
        if (por == Por.UNKNOWN) {
            return;
        }

        final int x = por.getX();
        final int y = por.getY();

        final boolean crossesBoundary = (por == Por.TRP || por == Por.BRP);

        // Calculate base row and column indices
        final int rowIndex = x + NUM_ROWS_IN_SLICE - 1;
        final int colIndex = crossesBoundary
                ? (y + NUM_COLS_IN_SLICE - 1) - pieces[x].length
                : (y + NUM_COLS_IN_SLICE - 1);

        // store the corners
        final double tlCorner = pieces[x][y];
        final double blCorner = pieces[rowIndex][y];
        final double trCorner = pieces[x][colIndex];
        final double brCorner = pieces[rowIndex][colIndex];

        final int middleRows = NUM_ROWS_IN_SLICE / 2;

        // Rotation crosses the array boundary
        if (crossesBoundary) {
            // Perform the in-place 180-degree rotation of the slice
            for (int i = 0; i < middleRows; i++) {
                for (int j = 0; j < NUM_COLS_IN_SLICE; j++) {
                    // Adjust colIndex to start from y instead of zero
                    int adjustedColIndex = colIndex - j >= 0 ? colIndex - j : y + NUM_COLS_IN_SLICE - 1 - j;

                    // Adjust y to start from zero instead of y
                    int adjustedYIndex = y + j < pieces[x + i].length ? y + j : j - (NUM_COLS_IN_SLICE / 2);

                    // Swap
                    double temp = pieces[rowIndex - i][adjustedColIndex];
                    pieces[rowIndex - i][adjustedColIndex] = pieces[x + i][adjustedYIndex];
                    pieces[x + i][adjustedYIndex] = temp;
                }
            }
        } else {
            // Rotation does not cross the array boundary

            // Perform the in-place 180-degree rotation of the slice
            for (int i = 0; i < middleRows; i++) {
                for (int j = 0; j < NUM_COLS_IN_SLICE; j++) {
                    // Swap
                    double temp = pieces[rowIndex - i][colIndex - j];
                    pieces[rowIndex - i][colIndex - j] = pieces[x + i][y + j];
                    pieces[x + i][y + j] = temp;
                }
            }
        }

        // Reverse the middle row of the slice
        final double[] row = pieces[x + Math.floorDiv(NUM_ROWS_IN_SLICE, 2)];
        final int middle = NUM_COLS_IN_SLICE / 2;
        for (int i = 0; i < middle; i++) {
            // Swap
            double temp = row[y + i];
            row[y + i] = row[colIndex - i];
            row[colIndex - i] = temp;
        }

        // Restore the corners
        pieces[x][y] = tlCorner;
        pieces[rowIndex][y] = blCorner;
        pieces[x][colIndex] = trCorner;
        pieces[rowIndex][colIndex] = brCorner;
    }

    @Override
    public void rotate(List<Por> moves) {
        for(Por move : moves) {
            rotate(move);
        }
    }

    @Override
    public void rotate(Por... moves) {
        for (Por move : moves) {
            rotate(move);
        }
    }

    @Override
    public void rotateUnrolled(final Por por) {
        // No check for Por.UNKNOWN
        double temp;

        int x = por.getX();
        int y = por.getY();
        int xp0 = x;
        int xp1 = x + 1;
        int xp2 = x + 2;
        int xp3 = x + 3;
        int xp4 = x + 4;
        int xp5 = x + 5;
        int xp6 = x + 6;

        int yp0 = y;
        int yp1 = y + 1;
        int yp2 = y + 2;
        int yp3 = y + 3;
        int yp4 = y + 4;
        int yp5 = y + 5;
        int yp6 = y + 6;
        int yp7 = y + 7;

        final boolean crossesBoundary = (por == Por.TRP || por == Por.BRP);

        // Edge case rotation
        if (crossesBoundary) {
            yp4 = 0;
            yp5 = 1;
            yp6 = 2;
            yp7 = 3;
        }

        // Contiguous rotation

        // Row 0
        // x + 0, y + 2 <-> x + 6, y + 5
        temp = pieces[xp0][yp2];
        pieces[xp0][yp2] = pieces[xp6][yp5];
        pieces[xp6][yp5] = temp;

        // x + 0, y + 5 <-> x + 6, y + 2
        temp = pieces[xp0][yp5];
        pieces[xp0][yp5] = pieces[xp6][yp2];
        pieces[xp6][yp2] = temp;

        // Row 1
        // x + 1, y + 1 <-> x + 5, y + 6
        temp = pieces[xp1][yp1];
        pieces[xp1][yp1] = pieces[xp5][yp6];
        pieces[xp5][yp6] = temp;

        // x + 1, y + 3 <-> x + 5, y + 4
        temp = pieces[xp1][yp3];
        pieces[xp1][yp3] = pieces[xp5][yp4];
        pieces[xp5][yp4] = temp;

        // x + 1, y + 4 <-> x + 5, y + 3
        temp = pieces[xp1][yp4];
        pieces[xp1][yp4] = pieces[xp5][yp3];
        pieces[xp5][yp3] = temp;

        // x + 1, y + 6 <-> x + 5, y + 1
        temp = pieces[xp1][yp6];
        pieces[xp1][yp6] = pieces[xp5][yp1];
        pieces[xp5][yp1] = temp;

        // Row 2
        // x + 2, y + 0 <-> x + 4, y + 7
        temp = pieces[xp2][yp0];
        pieces[xp2][yp0] = pieces[xp4][yp7];
        pieces[xp4][yp7] = temp;

        // x + 2, y + 2 <-> x + 4, y + 5
        temp = pieces[xp2][yp2];
        pieces[xp2][yp2] = pieces[xp4][yp5];
        pieces[xp4][yp5] = temp;

        // x + 2, y + 5 <-> x + 4, y + 2
        temp = pieces[xp2][yp5];
        pieces[xp2][yp5] = pieces[xp4][yp2];
        pieces[xp4][yp2] = temp;

        // x + 2, y + 7 <-> x + 4, y + 0
        temp = pieces[xp2][yp7];
        pieces[xp2][yp7] = pieces[xp4][yp0];
        pieces[xp4][yp0] = temp;

        // Row 3
        // x + 3, y + 1 <-> x + 3, y + 6
        temp = pieces[xp3][yp1];
        pieces[xp3][yp1] = pieces[xp3][yp6];
        pieces[xp3][yp6] = temp;

        // x + 3, y + 3 <-> x + 3, y + 4
        temp = pieces[xp3][yp3];
        pieces[xp3][yp3] = pieces[xp3][yp4];
        pieces[xp3][yp4] = temp;
    }

    /**
     * Creates a "deep copy" of this object.
     *
     * @return a deep copy of this object.
     */
    @Override
    public PentastickUsingDoubles deepCopy() {
        return new PentastickUsingDoubles(this.pieces);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PentastickUsingDoubles that)) {
            return false;
        }

        return Arrays.deepEquals(this.pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(pieces);
    }

    @Override
    public void scramble(final int numMoves) {
        int prev = 0;
        int curr = 0;

        for (int i = 0; i < numMoves; i++) {
            // Make sure we're not getting the same number twice
            // in a row. This would result in a wasted move.
            while (curr == prev) {
                curr = ThreadLocalRandom.current().nextInt(POR_LIST.size());
            }

            this.rotate(POR_LIST.get(curr));
            prev = curr;
        }
    }
}
