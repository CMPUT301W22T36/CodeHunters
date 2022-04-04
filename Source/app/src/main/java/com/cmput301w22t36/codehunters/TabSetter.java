package com.cmput301w22t36.codehunters;

import java.util.Observable;

public class TabSetter extends Observable {
    @Override
    public void notifyObservers(Object arg) {
        this.setChanged();
        super.notifyObservers(arg);
    }
}
