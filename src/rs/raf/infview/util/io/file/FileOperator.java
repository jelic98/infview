package rs.raf.infview.util.io.file;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Record;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractResource.SortType;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class FileOperator implements FileOperations, BulkReader {

    private AbstractResource file;

    public FileOperator(AbstractResource file) {
        this.file = file;
    }

    private boolean start() {
        boolean result = Context.instance().currentState.addFile(file);

        if(!result) {
            DialogAdapter.error(Res.STRINGS.ERROR_FILE_ALREADY_OPENED);
        }

        return result;
    }

    private void finish() {
        Context.instance().currentState.removeFile(file);
    }

    @Override
    public boolean create(Record record) {
        if(!start()) {
            return false;
        }

        boolean result = file.create(record);

        finish();

        return result;
    }

    @Override
    public Record read() {
        if(!start()) {
            return null;
        }

        Record result = file.read();

        finish();

        return result;
    }

    @Override
    public Set<Record> bulkRead() {
        if(!start()) {
            return null;
        }

        Set<Record> result = null;

        if(file instanceof BulkReader) {
             result = ((BulkReader) file).bulkRead();
        }

        finish();

        return result;
    }

    @Override
	public Set<Record> bulkSort(Map<String, SortType> map) {
    	if(!start()) {
            return null;
        }

        Set<Record> result = null;

        if(file instanceof BulkReader) {
             result = ((BulkReader) file).bulkSort(map);
        }

        finish();

        return result;
	}

	@Override
    public boolean update(Record oldRecord, Record newRecord) {
        if(!start()) {
            return false;
        }

        boolean result = file.update(oldRecord, newRecord);

        finish();

        return result;
    }

    @Override
    public boolean delete(Record record) {
        if(!start()) {
            return false;
        }

        boolean result = file.delete(record);

        finish();

        return result;
    }

    @Override
    public List<Record> search(Record record, boolean single) {
        if(!start()) {
            return null;
        }

        List<Record> result = file.search(record, single);

        finish();

        return result;
    }

    @Override
    public void sort(Map<String, AbstractResource.SortType> map) {
        if(!start()) {
            return;
        }

        file.sort(map);

        finish();
    }

    @Override
    public AbstractResource convert() {
        if(!start()) {
            return null;
        }

        AbstractResource result = file.convert();

        finish();

        return result;
    }
}