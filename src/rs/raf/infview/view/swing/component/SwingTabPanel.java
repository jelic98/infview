package rs.raf.infview.view.swing.component;

import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.PopupMenu;
import rs.raf.infview.view.component.TabPanel;
import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class SwingTabPanel extends JTabbedPane implements TabPanel {

    private PopupMenu menu;
    private final ChangeObservableDelegate<TabItem> delegate;

    public SwingTabPanel() {
    	delegate = new ChangeObservableDelegate<>();
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e)) {
                    if(menu != null) {
                        menu.show(e.getX(), e.getY(), e.getComponent());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if(SwingUtilities.isLeftMouseButton(e)) {
                    notifyObservers(ChangeType.SELECTION, null);
                }
            }
        });
    }

    @Override
    public void add(String title, byte[] icon, Component component) {
        addTab(title + " ", new ImageIcon(icon), (java.awt.Component) component);
    }

    @Override
    public void removeAt(int index) {
        removeTabAt(index);
    }

    @Override
    public void setPopupMenu(PopupMenu menu) {
        this.menu = menu;
    }

	@Override
	public void addObserver(ChangeObserver observer) {
		delegate.addObserver(observer);
	}

	@Override
	public void notifyObservers(ChangeType type, TabItem bundle) {
		delegate.notifyObservers(type, bundle);
	}

	@Override
	public void clear() {
		while(getTabCount() > 0) {
			remove(0);
		}
	}
}
