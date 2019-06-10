package rs.raf.infview.state;

import rs.raf.infview.util.io.file.AbstractResource;

class ReadyState extends State {

    @Override
    public boolean addFile(AbstractResource file) {
        Context.instance().currentState = new BusyState();

        return Context.instance().currentState.addFile(file);
    }

    @Override
    public boolean removeFile(AbstractResource file) {
        Context.instance().currentState = this;

        return true;
    }
}