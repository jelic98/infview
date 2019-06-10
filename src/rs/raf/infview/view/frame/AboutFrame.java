package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.adapter.ImageLabelFactory;
import rs.raf.infview.view.component.Label;
import rs.raf.infview.view.component.Panel;

public class AboutFrame extends FrameTemplate {

    public AboutFrame() {
        super(Res.STRINGS.FRAME_ABOUT);
    }

	@Override
	protected void configure() {
        setOrientation(Orientation.HORIZONTAL);
        setSizeRatio(0.4f);

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
        Label lblMember1 = App.UI.getLabel();
        lblMember1.setText(Res.STRINGS.ABOUT_AUTHOR_3);

        Label lblMember2 = App.UI.getLabel();
        lblMember2.setText(Res.STRINGS.ABOUT_AUTHOR_1);

        Label lblMember3 = App.UI.getLabel();
        lblMember3.setText(Res.STRINGS.ABOUT_AUTHOR_2);

        Panel member1 = App.UI.getPanel();
        member1.setLayout(Panel.Layout.NORMAL);
        member1.addComponent(new ImageLabelFactory().getImageLabel(Res.ICONS.IMAGE_AUTHOR_3));
        member1.addComponent(lblMember1);

        Panel member2 = App.UI.getPanel();
        member2.setLayout(Panel.Layout.NORMAL);
        member2.addComponent(new ImageLabelFactory().getImageLabel(Res.ICONS.IMAGE_AUTHOR_1));
        member2.addComponent(lblMember2);

        Panel member3 = App.UI.getPanel();
        member3.setLayout(Panel.Layout.NORMAL);
        member3.addComponent(new ImageLabelFactory().getImageLabel(Res.ICONS.IMAGE_AUTHOR_2));
        member3.addComponent(lblMember3);

        Panel team = App.UI.getPanel();
        team.setLayout(Panel.Layout.SHRINK);
        team.setPadding(20, 0, 20, 50);
        team.addComponent(member1);
        team.addComponent(member2);
        team.addComponent(member3);

        Label lblInfo = App.UI.getLabel();
        lblInfo.setText(getAboutInfo());

        Panel info = App.UI.getPanel();
        info.setLayout(Panel.Layout.SHRINK);
        info.setPadding(-20, 0, -50, 50);
        info.addComponent(lblInfo);

        Panel layout = App.UI.getPanel();
        layout.setLayout(Panel.Layout.NORMAL);
        layout.addComponent(info);
        layout.addComponent(team);

        frame.addComponent(layout);
	}

	private String getAboutInfo() {
        return "<html>" +
                "<p>" +
                Res.STRINGS.ABOUT_DESCRIPTION +
                "</p>" +
                "<br/>" +
                "<p>" +
                "<b>" +
                Res.STRINGS.ABOUT_VERSION +
                "</b>" +
                App.VERSION +
                "</p>" +
                "<p>" +
                "<b>" +
                Res.STRINGS.ABOUT_DEBUG +
                "</b>" +
                App.DEBUG +
                "</p>" +
                "</html>";
    }
}