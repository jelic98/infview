package rs.raf.infview.view.component;

public interface PopupMenu extends Component {

    void addComponent(Component component);
    void addSeparator();
    void show(int x, int y, Object parent);
}