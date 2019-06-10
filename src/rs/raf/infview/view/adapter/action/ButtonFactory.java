package rs.raf.infview.view.adapter.action;

import rs.raf.infview.core.App;
import rs.raf.infview.view.component.Button;

public class ButtonFactory {

	public Button getButton(ActionItem item) {
		Button button = App.UI.getButton();
		button.setText(item.getText());
		button.setHint(item.getText());
		button.setIcon(item.getIcon());
		button.addObserver(item.getAction());

		return button;
	}
}