package com.thetinkeringtypist.pentastick.instanceloader.examples;

import com.thetinkeringtypist.pentastick.instanceloader.InstanceLoadable;

public interface Shape extends InstanceLoadable {

    public int getNumSides();

    public String getColor();

    public void setColor(String color);

    @Override
    public boolean equals(Object obj);

    @Override
    public int hashCode();

    @Override
    public String toString();
}
