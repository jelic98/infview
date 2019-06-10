package rs.raf.infview.view.component;

public interface ToolBar extends Component {

    void addComponent(Component component);
    void addSeparator();
    void setFloatable(boolean floatable);
}