package rs.raf.infview.observer.command;

import org.json.JSONObject;
import rs.raf.infview.core.Paths;
import rs.raf.infview.model.Resource;
import rs.raf.infview.model.schema.SchemaSerializer;
import rs.raf.infview.model.validator.ValidationException;
import rs.raf.infview.model.validator.Validator;
import rs.raf.infview.observer.ChangeObservable;
import rs.raf.infview.observer.ChangeObservableDelegate;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;

import java.io.FileWriter;
import java.io.PrintWriter;

public class SaveSchemaAction extends Command implements ChangeObservable<Object> {

    private Resource resource;
    private String path;
    private final ChangeObservableDelegate<Object> delegate;

    public SaveSchemaAction(Resource resource, String path) {
        this.resource = resource;
        this.path = path;
        this.delegate = new ChangeObservableDelegate<>();
    }

    @Override
    public void execute() {
        try {
            new Validator().validate(resource);
        }catch(ValidationException e) {
            DialogAdapter.error(e.getMessage());
            return;
        }

        PrintWriter writer = null;

        try {
            JSONObject json = new SchemaSerializer().serialize(resource);

            Paths.create(path);

            writer = new PrintWriter(new FileWriter(path));
            writer.print(json.toString(4));
        }catch(Exception e) {
            DialogAdapter.error(e.getMessage());
            return;
        }finally {
            if(writer != null) {
                writer.close();
            }
        }

        notifyObservers(ChangeType.SAVE, null);
    }

    @Override
    public void addObserver(ChangeObserver observer) {
        delegate.addObserver(observer);
    }

    @Override
    public void notifyObservers(ChangeType type, Object bundle) {
        delegate.notifyObservers(type, bundle);
    }
}