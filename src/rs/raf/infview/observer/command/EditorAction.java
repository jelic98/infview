package rs.raf.infview.observer.command;

import rs.raf.infview.core.*;
import rs.raf.infview.model.Root;
import rs.raf.infview.model.Resource;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.ObservableSet;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.adapter.tab.TabItem;
import rs.raf.infview.view.component.Node;
import rs.raf.infview.view.frame.EditorFrame;

public class EditorAction extends Command {

    private Root root;
    private ObservableSet<TabItem> tabs;

    public EditorAction(Root root, ObservableSet<TabItem> tabs) {
        this.root = root;
        this.tabs = tabs;
    }

    @Override
    public void execute() {
        if(!App.RUNTIME.get(Parameters.SESSION_USER).equals(User.ADMINISTRATOR.name())) {
            DialogAdapter.error(Res.STRINGS.ERROR_RESTRICTED_ACCESS);
            return;
        }

        Node selected = Context.instance().tree.getSelectedNode();

        if(selected != null) {
            selected = selected.getDelegateModel();
        }

        if(!(selected instanceof Resource)) {
            DialogAdapter.error(Res.STRINGS.ERROR_RESOURCE_NOT_SELECTED);
            return;
        }

        Resource resource = (Resource) selected;

        for(TabItem tab : tabs) {
            if(resource.getEntities().contains(tab.getModel())) {
                DialogAdapter.error(Res.STRINGS.ERROR_ENTITY_OPENED);
                return;
            }
        }

        new EditorFrame(resource, root).open();
    }
}