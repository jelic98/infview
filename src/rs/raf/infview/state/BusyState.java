package rs.raf.infview.state;

import rs.raf.infview.util.io.file.AbstractResource;
import java.util.HashSet;
import java.util.Set;

class BusyState extends State {

    private Set<AbstractResource> files = new HashSet<>();

    @Override
    public boolean addFile(AbstractResource file) {
        return files.add(file);
    }

    @Override
    public boolean removeFile(AbstractResource file) {
        files.remove(file);

        if(files.isEmpty()) {
            Context.instance().currentState = new ReadyState();
        }

        return true;
    }
}