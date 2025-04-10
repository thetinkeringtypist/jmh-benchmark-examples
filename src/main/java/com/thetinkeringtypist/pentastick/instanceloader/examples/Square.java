package com.thetinkeringtypist.pentastick.instanceloader.examples;

import java.util.Objects;

public class Square implements Shape {

    private final int numSides = 4;
    private String color;

    public Square() {
        this.color = "";
    }

    public Square(String color) {
        this.color = color;
    }

    @Override
    public int getNumSides() {
        return 0;
    }

    @Override
    public String getColor() {
        return this.color;
    }

    @Override
    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Square that) ) {
            return false;
        }

        // numSides doesn't need to be in equals calculation

        return Objects.equals(this.color, that.color);
    }

    @Override
    public int hashCode() {
        int value = 97;
        value += 31 + color.hashCode() * 37;
        return value;
    }

    @Override
    public String toString() {
        return "Square [color=" + color + ", numSides=" + numSides + "]";
    }
}
