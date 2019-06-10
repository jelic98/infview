package rs.raf.infview.view.component;

import rs.raf.infview.observer.ChangeObservable;

public interface Frame extends ChangeObservable<Object> {

    void setTitle(String title);
    void setIcon(byte[] icon);
    void setSize(int width, int height);
    void setResizable(boolean resizable);
    void setVisible(boolean visible);
    void setFocused(boolean focused);
    void setMenuBar(MenuBar menuBar);
    int getInitialWidth();
    int getInitialHeight();
    void addComponent(Component component);
    void addComponent(Component component, Component.Position position);
    void refresh();
    void clear();
    void pack();
    void close();
}