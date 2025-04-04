package com.thetinkeringtypist.pentastick.lists;

class Piece {
    private short value;

    Piece() {
        this((short) 0);
    }

    public Piece(final short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Piece that)) {
            return false;
        }

        return this.value == that.value;
    }

    @Override
    public int hashCode() {
        return 509 + Integer.hashCode(this.value) * 863;
    }

    public short getValue() {
        return this.value;
    }

    public void setValue(final short value) {
        this.value = value;
    }
}