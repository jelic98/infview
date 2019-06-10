package rs.raf.infview.observer;

import java.util.HashSet;

public class ChangeObservableDelegate<T> implements ChangeObservable<T> {

    private final HashSet<ChangeObserver> observers;

    public ChangeObservableDelegate() {
        observers = new HashSet<>();
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, T bundle) {
        for(ChangeObserver listener : observers) {
        	if(listener != null) {
        		listener.onChange(type, bundle);
        	}
        }
    }
}