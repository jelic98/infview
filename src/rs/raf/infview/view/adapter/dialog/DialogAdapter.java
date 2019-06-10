package rs.raf.infview.view.adapter.dialog;

import rs.raf.infview.core.App;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.util.Bundle;
import rs.raf.infview.util.log.Log;
import rs.raf.infview.core.Res;
import rs.raf.infview.view.component.*;

public class DialogAdapter {

    private static final Dialog dialog = App.UI.getDialog();

    public static void error(String message) {
        dialog.error(message, Res.STRINGS.DIALOG_ERROR, Res.ICONS.ERROR);
        Log.e(message);
    }

    public static void info(String message) {
        dialog.info(message, Res.STRINGS.DIALOG_INFO, Res.ICONS.INFO);
        Log.i(message);
    }

    public static void question(Question question) {
        question.onAction(dialog.question(question.getMessage(),
                Res.STRINGS.DIALOG_QUESTION,
                Res.ICONS.QUESTION,
                question.getOptions()));
    }

    public static String input(Input input) {
        return dialog.input(input.getMessage(), input.getTitle());
    }

    public static void selection(Question question, String[] values, Bundle result) {
        ComboBox<String> combo = App.UI.getComboBox();
        combo.setItems(values);
        combo.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type) {
                if(type == ChangeType.SELECTION) {
                    result.setValue(combo.getSelectedItem());
                }
            }
        });

        Label lblMessage = App.UI.getLabel();
        lblMessage.setPosition(Component.Position.LEFT);
        lblMessage.setText(question.getMessage());

        Panel panel = App.UI.getPanel();
        panel.setLayout(Panel.Layout.EXPAND);
        panel.addComponent(lblMessage, Component.Position.TOP);
        panel.addComponent(combo, Component.Position.CENTER);

        question.onAction(dialog.selection(question.getMessage(),
                Res.STRINGS.DIALOG_QUESTION,
                Res.ICONS.QUESTION,
                question.getOptions(),
                panel));
    }
}