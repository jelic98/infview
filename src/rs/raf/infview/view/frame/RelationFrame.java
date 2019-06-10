package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Relation;
import rs.raf.infview.model.Resource;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.SaveRelationAction;
import rs.raf.infview.view.adapter.SchemaPanelFactory;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.component.Panel;

public class RelationFrame extends FrameTemplate {

    private Relation relation;
    private SchemaPanelFactory panelFactory;

    public RelationFrame(Resource resource, Relation relation) {
        super(Res.STRINGS.FRAME_RELATION, true);

        this.relation = relation;

        panelFactory = new SchemaPanelFactory(resource);
    }

	@Override
	protected void configure() {
        setSizeRatio(0.3f);

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
        Panel row = App.UI.getPanel();
        row.setLayout(Panel.Layout.SHRINK);
        row.addComponent(new ButtonFactory().getButton(new ActionItem(
                Res.STRINGS.MENU_SAVE,
                Res.ICONS.SAVE,
                new SaveRelationAction(relation, this))));

        Panel layout = App.UI.getPanel();
        layout.setPadding(20, 20, 20, 20);
        layout.setLayout(Panel.Layout.NORMAL);
        layout.addComponent(panelFactory.getPanel(relation));
        layout.addComponent(row);

        frame.addComponent(layout);
	}
}