package rs.raf.infview.state;

import rs.raf.infview.util.io.file.AbstractResource;

public abstract class State {

    public abstract boolean addFile(AbstractResource file);
    public abstract boolean removeFile(AbstractResource file);
}