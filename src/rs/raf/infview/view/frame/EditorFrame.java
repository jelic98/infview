package rs.raf.infview.view.frame;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Paths;
import rs.raf.infview.core.Res;
import rs.raf.infview.state.Context;
import rs.raf.infview.model.AbstractModel;
import rs.raf.infview.model.Relation;
import rs.raf.infview.model.Root;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Resource;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.observer.command.*;
import rs.raf.infview.view.adapter.SchemaPanelFactory;
import rs.raf.infview.view.adapter.action.ActionItem;
import rs.raf.infview.view.adapter.action.ButtonFactory;
import rs.raf.infview.view.component.*;
import java.util.Iterator;

public class EditorFrame extends FrameTemplate {

    private Resource resource;
    private Root root;
    private SchemaPanelFactory panelFactory;
    private Node selected;
    private SplitPanel splitMain;

    public EditorFrame(Resource resource, Root root) {
        super(Res.STRINGS.FRAME_EDITOR, true);

        this.resource = (Resource) resource.getClone();
        this.root = root;

        panelFactory = new SchemaPanelFactory(this.resource, new ChangeObserverAdapter() {
            @Override
            public void onChange() {
                splitMain.setRight(createEditorPanel(selected));
            }
        });
    }

	@Override
	protected void configure() {
        setSizeRatio(0.5f);

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
        ScrollPanel treePanel = App.UI.getScrollPanel();
        treePanel.addComponent(getTree(new Root(resource)));

        splitMain = App.UI.getSplitPanel();
        splitMain.setLeft(treePanel);
        splitMain.setRight(createBlankPanel());
        splitMain.setResizeWeight(0.25);

        frame.addComponent(splitMain, Component.Position.CENTER);
    }

    private Panel createEditorPanel(Node selected) {
        AbstractModel model = (AbstractModel) selected.getDelegateModel();

        if(model instanceof Root) {
            return createBlankPanel();
        }

        ButtonFactory btnFactory = new ButtonFactory();

        SaveSchemaAction saveAction = new SaveSchemaAction(resource, Paths.getResourcePath());
        saveAction.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange() {
                Iterator<Node> i = root.getChildren().iterator();

                while(i.hasNext()) {
                    if(i.next().getDelegateModel().equals(resource)) {
                        i.remove();
                        break;
                    }
                }

                root.addChild(resource);

                Context.instance().tree.reload();

                close();
            }
        });

        Panel row = App.UI.getPanel();
        row.setLayout(Panel.Layout.SHRINK);
        row.addComponent(btnFactory.getButton(new ActionItem(
                Res.STRINGS.MENU_SAVE,
                Res.ICONS.SAVE,
                saveAction)));

        if(model instanceof Attribute) {
            Attribute attribute = (Attribute) model;

            if(!attribute.isRelation()) {
                attribute.setRelation(new Relation());
            }

            row.addComponent(btnFactory.getButton(new ActionItem(
                    Res.STRINGS.MENU_RELATION,
                    Res.ICONS.RELATION,
                    new RelationAction(resource, attribute.getRelation()))));
        }

        Panel layout = App.UI.getPanel();
        layout.setPadding(20, 20, 20, 20);
        layout.setLayout(Panel.Layout.NORMAL);
        layout.addComponent(panelFactory.getPanel(model));
        layout.addComponent(row);

        return layout;
    }

    private Panel createBlankPanel() {
        return App.UI.getPanel();
    }

    private Tree getTree(Root root) {
        Tree tree = App.UI.getTree();
        tree.setRoot(root);
        tree.addObserver(new ChangeObserverAdapter() {
            @Override
            public void onChange(ChangeType type, Object bundle) {
                if(bundle instanceof Node) {
                    if(type == ChangeType.SELECTION) {
                        tree.addSelectedNode(selected);

                        splitMain.setRight(createEditorPanel(selected = (Node) bundle));
                    }else if(type == ChangeType.DESELECTION) {
                        tree.removeSelectedNode((Node) bundle);
                    }
                }
            }
        });

        tree.reload();

        return tree;
    }
}