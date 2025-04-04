package com.thetinkeringtypist.pentastick.arrays;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * The enumeration of a plane of rotation (POR)
 */
public enum Por {
    TLP("TL'", 0, 1),
    BLP("BL'", 4, 1),
    TL("TL", 0, 6),
    BL("BL", 4, 6),
    TF("TF", 0, 11),
    BF("BF", 4, 11),
    TR("TR", 0, 16),
    BR("BR", 4, 16),
    TRP("TR'", 0, 21),
    BRP("BR'", 4, 21),

    /**
     * Unknown is only here to make the compiler happy for the switch statement in fromValue()
     */
    UNKNOWN("UNK", -1, -1);

    private static final Map<Por, Set<Por>> COMPLIMENTS = new EnumMap<>(Por.class);
    private static final Set<Por> ALL_OF;

    static {
        // Populate compliments of pors
        Set<Por> allof = EnumSet.allOf(Por.class);

        for (Por por : allof) {
            EnumSet<Por> singletonSet = EnumSet.of(por);
            EnumSet<Por> compliment = EnumSet.complementOf(singletonSet);
            compliment.remove(UNKNOWN);
            COMPLIMENTS.put(por, Collections.unmodifiableSet(compliment));
        }

        allof.remove(UNKNOWN);
        ALL_OF = Collections.unmodifiableSet(allof);
    }

    private final String notation;
    private final int x, y;

    Por(String notation, int x, int y) {
        this.notation = notation;
        this.x = x;
        this.y = y;
    }

    public static Set<Por> complimentOf(Por por) {
        return COMPLIMENTS.get(por);
    }

    public static Set<Por> allOf() {
        return ALL_OF;
    }

    @Override
    public String toString() {
        return this.notation;
    }

    public String getNotation() {
        return this.notation;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}