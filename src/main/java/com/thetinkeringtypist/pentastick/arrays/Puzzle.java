package com.thetinkeringtypist.pentastick.arrays;

import java.util.List;

public interface Puzzle {
    @Override
    String toString();

    void rotate(final Por por);

    void rotate(final List<Por> moves);

    void rotate(final Por... moves);

    Puzzle deepCopy();

    @Override
    boolean equals(final Object obj);

    @Override
    int hashCode();

    void scramble(final int numMoves);

    void rotateUnrolled(final Por por);
}
