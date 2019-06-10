package rs.raf.infview.util;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.ChangeObservable;
import rs.raf.infview.observer.ChangeObserver;

import java.util.Iterator;
import java.util.TreeSet;

public class ObservableSet<T> extends TreeSet<T> implements ChangeObservable<T> {

    private final ChangeObservableDelegate<T> delegate;

    public ObservableSet() {
        delegate = new ChangeObservableDelegate<>();
    }

    @Override
    public boolean add(T t) {
        boolean result = super.add(t);

        notifyObservers(ChangeType.ADDITION, t);

        return result;
    }

    public void delete(T t) {
        remove(t);

        notifyObservers(ChangeType.REMOVAL, t);
    }

    @Override
	public void clear() {
		Iterator<T> i = iterator();

        while(i.hasNext()) {
            T item = i.next();

            i.remove();

            notifyObservers(ChangeType.REMOVAL, item);
        }
	}

	@Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, T bundle) {
        delegate.notifyObservers(type, bundle);
    }
}