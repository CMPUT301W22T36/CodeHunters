package com.cmput301w22t36.codehunters;

import java.util.Observable;


/**
 * Including a tabsetter in a fragment will allow for the parent fragment to listen for changes in
 * which tab is active to update UI elements
 */
public class TabSetter extends Observable {
    /**
     * call this when the activity is resumed with the code for that fragment in the argument
     * This notifies the parent activity/fragment of the new tab being opened
     * @param arg the argument for which tab has been opened
     */
    @Override
    public void notifyObservers(Object arg) {
        this.setChanged();
        super.notifyObservers(arg);
    }
}
