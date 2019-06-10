package rs.raf.infview.state;

import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.FileMatcher;
import rs.raf.infview.view.component.Tree;

public class Context {

    private static volatile Context instance;

    public Tree tree;
    public FileMatcher fileMatcher;
    public Entity entity;
    public Record record;
    public State currentState;

    public void reset() {
        tree = null;
        fileMatcher = null;
        entity = null;
        record = null;
        currentState = new ReadyState();
    }

    private Context() {
        reset();
    }

    public static Context instance() {
        if(instance == null) {
            Context.synchronize();
        }

        return instance;
    }
    
    private static synchronized void synchronize() {
        if(instance == null) {
            instance = new Context();
        }
    }
}