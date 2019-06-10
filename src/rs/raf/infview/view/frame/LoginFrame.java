package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Parameters;
import rs.raf.infview.core.Res;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.Command;
import rs.raf.infview.util.api.RequestBuilder;
import rs.raf.infview.util.api.ResponseListener;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.component.*;
import java.io.IOException;

public class LoginFrame extends FrameTemplate implements ResponseListener {

    public LoginFrame() {
        super(Res.STRINGS.FRAME_LOGIN);
    }

	@Override
	protected void configure() {
        frame.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type) {
                if(type == ChangeType.CLOSE) {
                    close();
                }
            }
        });
	}
	
	@Override
	protected void populate() {
        Panel layout = App.UI.getPanel();
        layout.setPadding(20, 20, 20, 20);
        layout.setLayout(Panel.Layout.NORMAL);

        Label lblUsername = App.UI.getLabel();
        lblUsername.setText(Res.STRINGS.INPUT_USERNAME);
        layout.addComponent(lblUsername);

	    TextField tfUsername = App.UI.getTextField();
	    tfUsername.setColumns(10);
        layout.addComponent(tfUsername);

        Label lblPassword = App.UI.getLabel();
        lblPassword.setText(Res.STRINGS.INPUT_PASSWORD);
        layout.addComponent(lblPassword);

        PasswordField pfPassword = App.UI.getPasswordField();
        pfPassword.setColumns(10);
        layout.addComponent(pfPassword);

        layout.addComponent(new ButtonFactory().getButton(new ActionItem(Res.STRINGS.OPTION_LOGIN, new Command() {
            @Override
            public void execute() {
                login(tfUsername.getText(), pfPassword.getText());
            }
        })));

        frame.addComponent(layout);
        frame.pack();
	}

    @Override
    public void onResponse(int code, String content) {
        if(code == 200) {
            onSuccess(content);
        }else {
            onError(content);
        }
    }

    private void login(String username, String password) {
        if(username.isEmpty() || password.isEmpty()) {
            onError(Res.STRINGS.ERROR_CREDENTIALS_REQUIRED);
            return;
        }

        try {
            new RequestBuilder(App.LOGIN_URL)
                    .addParameter("username", username)
                    .addParameter("password", password)
                    .setListener(this)
                    .build();
        }catch(IOException e) {
            onError(Res.STRINGS.ERROR_LOGIN_FAILED);
        }
    }

    private void onSuccess(String content) {
        App.RUNTIME.set(Parameters.SESSION_HASH, content.substring(0, content.indexOf(' ')));
        App.RUNTIME.set(Parameters.SESSION_USER, content.substring(content.indexOf(' ') + 1));

        new MainFrame().open();

        close();
    }

    private void onError(String message) {
        DialogAdapter.error(message);
    }
}