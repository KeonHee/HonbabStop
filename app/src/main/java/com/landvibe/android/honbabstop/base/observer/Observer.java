package com.landvibe.android.honbabstop.base.observer;

/**
 * Created by user on 2017-02-16.
 */

public interface Observer {
    void addObserver(CustomObserver observer);
    void removeObserver(CustomObserver observer);
    void notifyObservers();
}
