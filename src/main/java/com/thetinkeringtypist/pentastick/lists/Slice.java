package com.thetinkeringtypist.pentastick.lists;

import java.util.ArrayList;
import java.util.List;

/**
 * The implementation of a plane of rotation (POR).
 */
class Slice {

    private static final List<Integer> ranges = List.of(4, 6, 8, 8, 8, 6, 4);
    private static final List<Integer> yOffsets = List.of(0, -1, -2, -2, -2, -1, 0);
    private final Por por;
    // This could be an array instead of a list, but being able to use sublist is a nice feature.
    List<List<Piece>> pieces;

    public Slice(final Por por) {
        this(por, List.of());
    }

    public Slice(final Por por, List<List<Piece>> statePieces) {
        this.por = por;

        if (statePieces.isEmpty()) {
            this.pieces = new ArrayList<>();
        } else {
            pieces = new ArrayList<>(7);

            int x = por.getX();
            int y = por.getY();
            int j = 0;
            for (int i = x; i < x + 7; i++) {
                List<Piece> pRow = statePieces.get(i);
                int start = y + yOffsets.get(j);
                int end = Math.min(start + ranges.get(j), pRow.size());

                List<Piece> sublist;

                // POR row does not cross the start/end puzzle boundary.
                // Use a sublist of the underlying puzzle row.
                if (pRow.size() != end) {
                    sublist = pRow.subList(start, end);
                } else {
                    // POR row crosses the start/end puzzle boundary (TR' or BR').
                    // Create a new list in which to store the end and start sublists.
                    sublist = new ArrayList<>(pRow.subList(start, end));
                    sublist.addAll(pRow.subList(0, Math.abs((end - 1) - start)));
                }

                pieces.add(sublist);
                j++;
            }
        }
    }

    @Override
    public String toString() {
        return this.por.toString();
    }

    /**
     * Rotates the pieces in this plane of rotation by 180 degrees. Simply speaking, this is done by doing two
     * separate operations: swapping columns and swapping rows. This could be done  all at once, bypassing sparing the
     * extra iterations. However, for a first try, this is simple and easy to explain.
     */
    public void rotate() {
        // 1) Swapping columns, ignoring the middle column
        //      Only iterate over the columns up to the middle, otherwise
        //      they will swap back to their original positions
        for (List<Piece> row : this.pieces) {
            int middle = row.size() / 2;

            for (int i = 0; i < middle; i++) {
                int colIndex = row.size() - 1 - i;

                Piece ipiece = row.get(i);
                Piece cpiece = row.get(colIndex);

                short temp = ipiece.getValue();

                ipiece.setValue(cpiece.getValue());
                cpiece.setValue(temp);
            }
        }

        // 2) Swapping rows, ignore the middle row
        //      Only iterate over the rows up to the middle, otherwise
        //      they will swap back to their original positions
        int middle = this.pieces.size() / 2;

        for (int i = 0; i < middle; i++) {
            int rowIndex = this.pieces.size() - 1 - i;

            List<Piece> ilist = pieces.get(i);
            List<Piece> rlist = pieces.get(rowIndex);

            for (int j = 0; j < ilist.size(); j++) {
                Piece ipiece = ilist.get(j);
                short temp = ipiece.getValue();
                Piece rpiece = rlist.get(j);

                ipiece.setValue(rpiece.getValue());
                rpiece.setValue(temp);
            }
        }
    }
}