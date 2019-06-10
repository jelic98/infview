package rs.raf.infview.view.adapter.action;

import rs.raf.infview.core.App;
import rs.raf.infview.view.component.Component;
import rs.raf.infview.view.component.Menu;
import rs.raf.infview.view.component.MenuItem;
import rs.raf.infview.view.component.PopupMenu;

public class PopupMenuFactory {

	public PopupMenu getPopupMenu(CompositeActionItem[] items) {
        PopupMenu menu = App.UI.getPopupMenu();

        for(CompositeActionItem item : items) {
            menu.addComponent(getItems(item));

            if(item.isSeparated()) {
                menu.addSeparator();
            }
        }

        return menu;
	}

    private Component getItems(CompositeActionItem item) {
        if(item.isEmpty()) {
            MenuItem menuItem = App.UI.getMenuItem();
            menuItem.setText(item.getText());
            menuItem.addObserver(item.getAction());
            menuItem.setPadding(10, 10, 10, 0);
            return menuItem;
        }

        Menu menu = App.UI.getMenu();
        menu.setText(item.getText());

        for(CompositeActionItem child : item.getChildren()) {
            menu.addComponent(getItems(child));

            if(child.isSeparated()) {
                menu.addSeparator();
            }
        }

        return menu;
    }
}