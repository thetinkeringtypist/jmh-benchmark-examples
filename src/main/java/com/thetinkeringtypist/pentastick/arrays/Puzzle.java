package com.thetinkeringtypist.pentastick.arrays;

import java.util.List;

public interface Puzzle {
    @Override
    String toString();

    void rotate(Por por);

    void rotate(List<Por> moves);

    void rotate(Por... moves);

    Puzzle deepCopy();

    @Override
    boolean equals(final Object obj);

    @Override
    int hashCode();

    void scramble(final int numMoves);
}
