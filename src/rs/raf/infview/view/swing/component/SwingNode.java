package rs.raf.infview.view.swing.component;

import rs.raf.infview.model.Root;
import rs.raf.infview.view.component.Node;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import java.util.Enumeration;
import java.util.Set;
import java.util.TreeSet;

public class SwingNode implements MutableTreeNode, Node {

    private String nodeName;
    private byte[] icon;
    private Set<Node> children;
    private Node parent;
    private Node model;

    public SwingNode(String nodeName, byte[] icon) {
        this.nodeName = nodeName;
        this.icon = icon;
    }

    private void lazyLoadChildren() {
        if(children == null) {
            children = new TreeSet<>();
        }
    }

    @Override
    public String getNodeName() {
        return nodeName;
    }

    @Override
    public byte[] getIcon() {
        return icon;
    }

    @Override
    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    @Override
    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    @Override
    public void setAncestor(Node node) {
        this.parent = node;
    }

    @Override
    public boolean addChild(Node node) {
        lazyLoadChildren();

        if(node.equals(this) || (node instanceof Root && model instanceof Root)) {
            children.addAll(node.getChildren());
            return true;
        }

        node.setAncestor(this);

        return children.add(node.getDelegateNode());
    }

    @Override
    public TreeNode getChildAt(int childIndex) {
        if(!hasChildren()) {
            return null;
        }

        try {
            return (TreeNode) getChildren().toArray()[childIndex];
        }catch(Exception e) {
            return null;
        }
    }

    @Override
    public int getChildCount() {
        return getChildren().size();
    }

    @Override
    public int getIndex(TreeNode node) {
        int index = -1;

        for(Node child : getChildren()) {
            index++;

            if(child == node) {
                break;
            }
        }

        return index;
    }

    @Override
    public Enumeration children() {
        return (Enumeration) getChildren();
    }

    @Override
    public Node getAncestor() {
        return parent;
    }

    @Override
    public TreeNode getParent() {
        return (SwingNode) parent.getDelegateNode();
    }

    @Override
    public void setParent(MutableTreeNode newParent) {
        if(newParent instanceof Node) {
            parent = (Node) newParent;
        }
    }

    @Override
    public boolean getAllowsChildren() {
        return true;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    @Override
    public void insert(MutableTreeNode child, int index) {
        if(child instanceof Node) {
            addChild((Node) child);
        }
    }

    @Override
    public void remove(int index) {
        Node node = null;

        for(Node child : getChildren()) {
            if(index-- == 0) {
                node = child;
                break;
            }
        }

        if(node != null && hasChildren()) {
            children.remove(node);
        }
    }

    @Override
    public void remove(MutableTreeNode node) {
        if(node != null && hasChildren() && node instanceof Node) {
            children.remove(node);
        }
    }

    @Override
    public void removeFromParent() {
        ((SwingNode) parent).remove(this);
    }

    @Override
    public void setUserObject(Object object) {
        if(object instanceof String) {
            setNodeName((String) object);
        }
    }

    @Override
    public boolean hasChildren() {
        lazyLoadChildren();

        return !children.isEmpty();
    }

    @Override
    public void removeChildren() {
        if(hasChildren()) {
            children.clear();
        }
    }

    @Override
    public Set<Node> getChildren() {
        lazyLoadChildren();

        return children;
    }

    @Override
    public Node getDelegateNode() {
        return this;
    }

    @Override
    public Node getDelegateModel() {
        return model;
    }

    @Override
    public void setDelegateModel(Node node) {
        model = node;
    }

    @Override
    public int compareTo(Node node) {
        return getDelegateModel().compareTo(node.getDelegateModel());
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null || (obj instanceof Node)) {
            return false;
        }

        return getDelegateModel().equals(((Node) obj).getDelegateModel());
    }

    @Override
    public String toString() {
        return model.toString();
    }
}