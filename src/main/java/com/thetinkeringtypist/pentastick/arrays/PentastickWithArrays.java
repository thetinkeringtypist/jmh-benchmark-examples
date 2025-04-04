package com.thetinkeringtypist.pentastick.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class PentastickWithArrays implements Puzzle {

    private static final short[] WING_ROW = new short[] {0, 1, 0, 1, 0, 0, 2, 0, 2, 0, 0, 3, 0, 3, 0, 0, 4, 0, 4, 0, 0, 5, 0, 5, 0};
    private static final short[] EDGE_ROW = new short[] {1, 0, 1, 0, 1, 2, 0, 2, 0, 2, 3, 0, 3, 0, 3, 4, 0, 4, 0, 4, 5, 0, 5, 0, 5};
    private static final int NUM_ROWS_IN_SLICE = 7;
    private static final int NUM_COLS_IN_SLICE = 8;
    private static final List<Por> POR_LIST = new ArrayList<>(Por.allOf());

    /**
     * The pieces of pentastick. This starts off as the initial state of the puzzle and is updated with rotations
     */
    private final short[][] pieces;


    /**
     * Constructs a pentastick puzzle whose initial state is the solved state.
     */
    public PentastickWithArrays() {
        this(null);
    }

    public PentastickWithArrays(final short[][] initialState) {
        pieces = new short[11][25];

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

        for (short[] row : pieces) {
            builder.append(String.format("%3d | ", rowCounter));
            for (short p : row) {
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
        final short tlCorner = pieces[x][y];
        final short blCorner = pieces[rowIndex][y];
        final short trCorner = pieces[x][colIndex];
        final short brCorner = pieces[rowIndex][colIndex];

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
                    short temp = pieces[rowIndex - i][adjustedColIndex];
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
                    short temp = pieces[rowIndex - i][colIndex - j];
                    pieces[rowIndex - i][colIndex - j] = pieces[x + i][y + j];
                    pieces[x + i][y + j] = temp;
                }
            }
        }

        // Reverse the middle row of the slice
        final short[] row = pieces[x + Math.floorDiv(NUM_ROWS_IN_SLICE, 2)];
        final int middle = NUM_COLS_IN_SLICE / 2;
        for (int i = 0; i < middle; i++) {
            // Swap
            short temp = row[y + i];
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

    /**
     * Creates a "deep copy" of this object.
     *
     * @return a deep copy of this object.
     */
    @Override
    public PentastickWithArrays deepCopy() {
        return new PentastickWithArrays(this.pieces);
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PentastickWithArrays that)) {
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
