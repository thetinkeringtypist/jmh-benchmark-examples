package com.thetinkeringtypist.pentastick.lists;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class PentastickWithLists {

    /**
     * The pieces of pentastick. This starts off as the initial state of the puzzle and is updated with rotations
     */
    private final List<List<Piece>> pieces;

    private final Map<Por, Slice> slices;

    private final List<Por> porList;

    /**
     * Constructs a pentastick puzzle whose initial state is the solved state.
     */
    public PentastickWithLists() {
        this(List.of());
    }

    public PentastickWithLists(List<List<Short>> initialState) {
        this.pieces = buildDataStructure(initialState);

        // Define planes of rotation
        this.slices = new EnumMap<>(Por.class);
        this.slices.put(Por.TLP, new Slice(Por.TLP, this.pieces));
        this.slices.put(Por.BLP, new Slice(Por.BLP, this.pieces));
        this.slices.put(Por.TL, new Slice(Por.TL, this.pieces));
        this.slices.put(Por.BL, new Slice(Por.BL, this.pieces));
        this.slices.put(Por.TF, new Slice(Por.TF, this.pieces));
        this.slices.put(Por.BF, new Slice(Por.BF, this.pieces));
        this.slices.put(Por.TR, new Slice(Por.TR, this.pieces));
        this.slices.put(Por.BR, new Slice(Por.BR, this.pieces));
        this.slices.put(Por.TRP, new Slice(Por.TRP, this.pieces));
        this.slices.put(Por.BRP, new Slice(Por.BRP, this.pieces));
        this.slices.remove(Por.UNKNOWN);

        /** Need this list to get random PORs for scrambling the puzzle.
         * Can't use {@link Por#ofAll()} since it returns an EnumSet and we need
         * Can't use {@link Por#allOf()} since it returns an EnumSet and we need
         * a list to grab random indices from
         */
        this.porList = new ArrayList<>(this.slices.keySet());
    }

    private static List<List<Piece>> buildDataStructure(List<List<Short>> initialState) {
        List<List<Short>> state = initialState;

        if (Objects.isNull(state) || state.isEmpty()) {
            List<Short> wingRow = List.of((short) 0, (short) 1, (short) 0, (short) 1, (short) 0, (short) 0, (short) 2, (short) 0, (short) 2, (short) 0, (short) 0, (short) 3, (short) 0, (short) 3, (short) 0, (short) 0, (short) 4, (short) 0, (short) 4, (short) 0, (short) 0, (short) 5, (short) 0, (short) 5, (short) 0);
            List<Short> edgeRow = List.of((short) 1, (short) 0, (short) 1, (short) 0, (short) 1, (short) 2, (short) 0, (short) 2, (short) 0, (short) 2, (short) 3, (short) 0, (short) 3, (short) 0, (short) 3, (short) 4, (short) 0, (short) 4, (short) 0, (short) 4, (short) 5, (short) 0, (short) 5, (short) 0, (short) 5);

            state = new ArrayList<>(11);
            state.add(wingRow);
            state.add(edgeRow);
            state.add(wingRow);
            state.add(edgeRow);
            state.add(wingRow);
            state.add(edgeRow);
            state.add(wingRow);
            state.add(edgeRow);
            state.add(wingRow);
            state.add(edgeRow);
            state.add(wingRow);
        }

        // Convert a 2D list of integers to a 2D list of nodes
        List<List<Piece>> result = new ArrayList<>(11);
        for (List<Short> l : state) {
            result.add(l.stream().map(Piece::new).collect(Collectors.toList()));
        }

        return result;
    }

    @Override
    public String toString() {
        int rowCounter = 0;
        StringBuilder builder = new StringBuilder("    |");
        for (int i = 0; i < this.pieces.get(0).size(); i++) {
            builder.append(String.format("%3d", i)).append(" ");
        }

        builder.append("\n");
        builder.append(String.format("----+%s\n", "-".repeat(100)));

        for (List<Piece> row : this.pieces) {
            builder.append(String.format("%3d | ", rowCounter));
            for (Piece p : row) {

                builder.append(String.format("%2d ", p.getValue())).append(" ");
            }
            builder.delete(builder.length() - 2, builder.length() - 1);
            builder.append("\n");
            rowCounter++;
        }

        return builder.toString();
    }

    public List<List<Short>> getPieces() {
        // Convert a 2D list of pieces to a 2D list of integers
        List<List<Short>> result = new ArrayList<>(11);
        for (List<Piece> l : this.pieces) {
            result.add(l.stream().map(Piece::getValue).collect(Collectors.toList()));
        }

        return result;
    }

    /**
     * Rotates the pieces in the given POR by 180 degrees and takes note of the rotation.
     *
     * @param por the plane of rotation to manipulate.
     */
    public void rotate(final Por por) {
        Slice slice = slices.get(por);
        slice.rotate();
    }

    public void rotate(List<Por> moves) {
        for (Por p : moves) {
            rotate(p);
        }
    }

    /**
     * Creates a "deep copy" of this object.
     *
     * @return a deep copy of this object.
     */
    public PentastickWithLists deepCopy() {
        return new PentastickWithLists(this.getPieces());
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof PentastickWithLists that)) {
            return false;
        }

        return Objects.equals(this.pieces, that.pieces);
    }

    @Override
    public int hashCode() {
        return 19 + pieces.hashCode() * 717;
    }

    public void scramble(final int numMoves) {
        int prev = 0;
        int curr = 0;

        for (int i = 0; i < numMoves; i++) {
            // Make sure we're not getting the same number twice
            // in a row. This would result in a wasted move.
            while (curr == prev) {
                curr = ThreadLocalRandom.current().nextInt(porList.size());
            }

            this.rotate(porList.get(curr));
            prev = curr;
        }
    }
}