package rs.raf.infview.util.io.file.converter.tree;

import java.util.Map;

class BlockElement implements Comparable<BlockElement> {

    private Map<String, String> data;
    private long pointer;

    BlockElement(Map<String, String> data, long pointer) {
        this.data = data;
        this.pointer = pointer;
    }

    Map<String, String> getKeys() {
        return data;
    }

    long getPointer() {
        return pointer;
    }

    @Override
    public int compareTo(BlockElement element) {
        for(String key : data.keySet()) {
            String value1 = data.get(key);
            String value2 = element.data.get(key);

            int compare = value1.compareTo(value2);

            if(compare != 0) {
                return compare;
            }
        }

        return 0;
    }
}