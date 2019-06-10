package rs.raf.infview.util.io.file.converter.tree;

import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.SequentialFile;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class BlockTreeMaker {

    private static final int NODE_ELEMENT_COUNT = 2;

    public BlockTree makeTree(SequentialFile file, int blockSize) {
        List<BlockElement> elements = getElements(file, blockSize);
        List<BlockNode> leaves = getLeaves(elements);
        List<BlockNode> nodes = getNodes(leaves);
        BlockNode root = getRoot(nodes);

        return new BlockTree(root);
    }

    private BlockNode getRoot(List<BlockNode> nodes) {
        while(nodes.size() > 2) {
            nodes = getNodes(nodes);
        }

        BlockNode root = new BlockNode();

        for(BlockNode node : nodes) {
            root.add(node);
        }

        return root;
    }

    private List<BlockNode> getNodes(List<BlockNode> leaves) {
        List<BlockNode> nodes = new LinkedList<>();

        Iterator<BlockNode> iter = leaves.iterator();

        while(iter.hasNext()) {
            BlockNode node = new BlockNode();

            node.add(iter.next());

            if(iter.hasNext()) {
                node.add(iter.next());
            }

            nodes.add(node);
        }

        return nodes;
    }

    private List<BlockNode> getLeaves(List<BlockElement> elements) {
        List<BlockNode> leaves = new LinkedList<>();

        Iterator<BlockElement> iter = elements.iterator();

        while(iter.hasNext()) {
            boolean stop = false;

            BlockNode node = new BlockNode();

            for(int i = 0; i < NODE_ELEMENT_COUNT; i++) {
                try {
                    node.addElement(iter.next());
                }catch(NoSuchElementException e) {
                    stop = true;
                    break;
                }
            }

            leaves.add(node);

            if(stop) {
                break;
            }
        }

        return leaves;
    }

    private List<BlockElement> getElements(SequentialFile file, int blockSize) {
        List<BlockElement> elements = new LinkedList<>();

        int totalRecords = file.getInfo().get(AbstractResource.InfoEntry.TOTAL_RECORDS);

        file.resetPointer();

        for(int i = 0; i < totalRecords; i++) {
            long pointer = file.getPointer();

            Record record = file.read();

            if(record == null || i % blockSize != 0) {
                continue;
            }

            record.setAttributes(file.getAttributes());

            elements.add(new BlockElement(record.getPrimaries(), pointer));
        }

        return elements;
    }
}