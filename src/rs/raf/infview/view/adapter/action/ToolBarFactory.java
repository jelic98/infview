package rs.raf.infview.view.adapter.action;

import rs.raf.infview.core.App;
import rs.raf.infview.view.component.Button;
import rs.raf.infview.view.component.ToolBar;

public class ToolBarFactory {

	public ToolBar getToolBar(ActionItem[] items) {
		return getToolBar(items, false);
	}

	public ToolBar getToolBar(ActionItem[] items, boolean showText) {
        ToolBar bar = App.UI.getToolBar();
        bar.setFloatable(true);

		for(int i = 0; i < items.length; i++) {
			ActionItem item = items[i];

		    if(i > 0) {
                bar.addSeparator();
            }

			Button button = App.UI.getButton();
			button.setHint(item.getText());
			button.setIcon(item.getIcon());
			button.addObserver(item.getAction());

			if(showText) {
				button.setText(item.getText());
			}

			bar.addComponent(button);
		}

		return bar;
	}
}