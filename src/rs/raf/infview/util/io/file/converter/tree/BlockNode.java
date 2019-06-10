package rs.raf.infview.util.io.file.converter.tree;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

class BlockNode implements Comparable<BlockNode> {

    private List<BlockElement> elements;
    private BlockNode left, right;

    BlockNode() {
        elements = new LinkedList<>();
    }

    void add(BlockNode node) {
        if(left == null) {
            left = node;
            addElement(left.elements.get(0));
        }else if(right == null) {
            right = node;
            addElement(right.elements.get(0));
        }
    }

    void addElement(BlockElement element) {
        elements.add(element);
        elements.sort(Comparator.naturalOrder());
    }

    public List<BlockElement> getElements() {
        return elements;
    }

    public BlockNode getLeft() {
        return left;
    }

    void setLeft(BlockNode left) {
        this.left = left;
    }

    public BlockNode getRight() {
        return right;
    }

    void setRight(BlockNode right) {
        this.right = right;
    }

    boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(BlockNode node) {
        for(int i = 0; i < elements.size(); i++) {
            BlockElement value1;
            BlockElement value2;

            try {
                value1 = elements.get(i);
                value2 = node.elements.get(i);
            }catch(IndexOutOfBoundsException e) {
                int size1 = elements.size();
                int size2 = node.elements.size();

                return size1 - size2;
            }

            int compare = value1.compareTo(value2);

            if(compare != 0) {
                return compare;
            }
        }

        return 0;
    }
}