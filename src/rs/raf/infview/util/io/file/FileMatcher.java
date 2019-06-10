package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Entity;
import java.util.HashMap;
import java.util.Map;

public class FileMatcher {

    private Map<Entity, AbstractResource> items;

    public FileMatcher() {
        items = new HashMap<>();
    }

    public AbstractResource get(Entity entity) {
        return items.get(entity);
    }

    public void add(Entity entity, AbstractResource file) {
        items.put(entity, file);
    }

    public void closeAll() {
        for(Entity entity : items.keySet()) {
            items.get(entity).close();
        }
    }
}