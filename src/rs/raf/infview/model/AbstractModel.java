package rs.raf.infview.model;

import rs.raf.infview.core.App;
import rs.raf.infview.model.schema.SchemaSerializable;
import rs.raf.infview.model.validator.Validateable;
import rs.raf.infview.observer.ChangeObservable;
import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.component.Node;
import java.util.*;

public abstract class AbstractModel implements Node, ChangeObservable<AbstractModel>,
        Validateable, SchemaSerializable, DeepCopy<AbstractModel> {

    private Node node;
    private Validateable validateableParent;
    private transient ChangeObservableDelegate<Node> delegate;

    AbstractModel(String name, byte[] icon) {
        node = App.UI.getNode(name, icon);

        setDelegateModel(this);
    }

    AbstractModel(AbstractModel original) {
        this(original.getNodeName(), original.getIcon());

        this.validateableParent = original.validateableParent;
    }

    public abstract AbstractModel getChild(String name);
    public abstract String getChildType();
    public abstract int getLevel();

    @Override
    public boolean addChild(Node node) {
        boolean result = this.node.addChild(node);

        ((AbstractModel) node.getDelegateModel()).validateableParent = this;

        notifyObservers(ChangeType.UPDATE, this);

        return result;
    }

    @Override
    public void removeChildren() {
        node.removeChildren();

        notifyObservers(ChangeType.REMOVAL, this);
    }

    @Override
    public int compareTo(Node node) {
        return this.node.compareTo(node);
    }

    private void lazyLoadDelegate() {
        if(delegate == null) {
            delegate = new ChangeObservableDelegate<>();
        }
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        lazyLoadDelegate();

        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, AbstractModel bundle) {
        lazyLoadDelegate();

        delegate.notifyObservers(type, bundle);
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof AbstractModel)) {
            return false;
        }

        AbstractModel model = (AbstractModel) obj;

        return getNodeName().equals(model.getNodeName());
    }

    @Override
    public String toString() {
        return getNodeName();
    }

    @Override
    public String getNodeName() {
        return node.getNodeName();
    }

    @Override
    public void setNodeName(String nodeName) {
        node.setNodeName(nodeName);

        notifyObservers(ChangeType.UPDATE, this);
    }

    @Override
	public Validateable getParent() {
		return validateableParent;
	}

	@Override
    public byte[] getIcon() {
        return node.getIcon();
    }

    @Override
    public void setIcon(byte[] icon) {
        node.setIcon(icon);
    }

    @Override
    public boolean hasChildren() {
        return node.hasChildren();
    }

    @Override
    public Set<Node> getChildren() {
        return node.getChildren();
    }

    @Override
    public int getChildCount() {
        return node.getChildCount();
    }

    @Override
    public Node getAncestor() {
        return node.getAncestor();
    }

    @Override
    public void setAncestor(Node node) {
        this.node.setAncestor(node);
    }

    @Override
    public void removeFromParent() {
        node.removeFromParent();
    }

    @Override
    public Node getDelegateNode() {
        return node.getDelegateNode();
    }

    @Override
    public Node getDelegateModel() {
        return this;
    }

    @Override
    public void setDelegateModel(Node node) {
        this.node.setDelegateModel(node);
    }
}