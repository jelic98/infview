package rs.raf.infview.util.io.file.converter.tree;

import java.util.*;

public class BlockTree {

    private BlockNode root;

    BlockTree(BlockNode root) {
        this.root = root;
    }

    public List<List<String>> getBlocks() {
        List<List<String>> blocks = new LinkedList<>();

        List<BlockNode> nodes = new LinkedList<>();
        extractLeaves(root, nodes);
        nodes.sort(new NodePointerComparator());

        for(BlockNode node : nodes) {
            List<BlockElement> elements = node.getElements();
            elements.sort(new ElementPointerComparator());

            for(BlockElement element : elements) {
                List<String> block = new LinkedList<>();
                Map<String, String> data = element.getKeys();

                for(String key : data.keySet()) {
                    block.add(data.get(key));
                }

                block.add(String.valueOf(element.getPointer()));
                blocks.add(block);
            }
        }

        return blocks;
    }

    private void extractLeaves(BlockNode node, List<BlockNode> nodes) {
        if(node == null) {
            return;
        }

        if(node.isLeaf()) {
            nodes.add(node);
            return;
        }

        extractLeaves(node.getLeft(), nodes);
        extractLeaves(node.getRight(), nodes);
    }

    private static class NodePointerComparator implements Comparator<BlockNode> {

        private final ElementPointerComparator comparator = new ElementPointerComparator();

        @Override
        public int compare(BlockNode n1, BlockNode n2) {
            BlockElement e1 = n1.getElements().get(0);
            BlockElement e2 = n2.getElements().get(0);

            return comparator.compare(e1, e2);
        }
    }

    private static class ElementPointerComparator implements Comparator<BlockElement> {

        @Override
        public int compare(BlockElement e1, BlockElement e2) {
            long p1 = e1.getPointer();
            long p2 = e2.getPointer();

            return (int) (p1 - p2);
        }
    }
}