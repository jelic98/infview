package rs.raf.infview.view.component;

public interface Panel extends Component {

    enum Layout {
        EXPAND,
        NORMAL,
        SHRINK
    }

    void addComponent(Component component);
    void addComponent(Component component, Position position);
    void setPadding(int top, int right, int bottom, int left);
    void setLayout(Layout layout);
    void clear();
}