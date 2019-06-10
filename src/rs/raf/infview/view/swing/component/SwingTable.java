package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.Table;
import rs.raf.infview.view.component.TableModel;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SwingTable extends JTable implements Table {
	
	private final ChangeObservableDelegate<Integer> delegate;

	private SwingTableModel model;

    public SwingTable() {
    	delegate = new ChangeObservableDelegate<>();
    	
        setFillsViewportHeight(true);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				notifyObservers(ChangeType.SELECTION, getSelectedRow());
			}
		});
    }

    @Override
    public void refresh() {
        model.fireTableDataChanged();
    }

    @Override
    public void setModel(TableModel model) {
        this.model = new SwingTableModel(model);

        setModel(this.model);
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, Integer bundle) {
        delegate.notifyObservers(type, bundle);
    }
}