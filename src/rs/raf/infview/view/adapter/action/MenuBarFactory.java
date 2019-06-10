package rs.raf.infview.view.adapter.action;

import rs.raf.infview.core.App;
import rs.raf.infview.observer.command.CommandQueue;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.Menu;
import rs.raf.infview.view.component.MenuBar;
import rs.raf.infview.view.component.MenuItem;

public class MenuBarFactory {

	public MenuBar getMenuBar(CompositeActionItem[] items) {
        MenuBar bar = App.UI.getMenuBar();

        for(CompositeActionItem item : items) {
            bar.addComponent(getItems(item, true));

            if(item.isSeparated()) {
                bar.addGlue();
            }
        }

        return bar;
	}

	private Component getItems(CompositeActionItem item, boolean topLevel) {
	    if(item.isEmpty() && !topLevel) {
            MenuItem menuItem = App.UI.getMenuItem();
            menuItem.setText(item.getText());
            menuItem.setIcon(item.getIcon());
            menuItem.setMnemonic(item.getMnemonic());
            menuItem.setAccelerator(item.getAccelerator());
            menuItem.addObserver(item.getAction());
            menuItem.setPadding(10, 10, 10, 0);
            return menuItem;
        }

        Menu menu = App.UI.getMenu();
	    menu.setText(item.getText());
	    menu.setMnemonic(item.getMnemonic());
        menu.setIcon(item.getIcon());
        
        if(item.getAction() != null) {
        	menu.addObserver(new ChangeObserverAdapter() {
                @Override
                public void onChange() {
                    CommandQueue.push(item.getAction());
                }
            });
        }

	    for(CompositeActionItem child : item.getChildren()) {
	        menu.addComponent(getItems(child, false));

            if(child.isSeparated()) {
                menu.addSeparator();
            }
        }

        return menu;
    }
}