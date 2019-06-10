package rs.raf.infview.view.component;

import java.io.Serializable;
import java.util.Set;

public interface Node extends Serializable, Comparable<Node> {

    long serialVersionUID = 1L;

    String getNodeName();
    byte[] getIcon();
    void setNodeName(String nodeName);
    void setIcon(byte[] icon);
    boolean addChild(Node node);
    void removeChildren();
    boolean hasChildren();
    Set<Node> getChildren();
    int getChildCount();
    Node getAncestor();
    void setAncestor(Node node);
    void removeFromParent();
    Node getDelegateNode();
    Node getDelegateModel();
    void setDelegateModel(Node node);
}