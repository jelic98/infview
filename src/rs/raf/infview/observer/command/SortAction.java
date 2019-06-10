package rs.raf.infview.observer.command;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.Resource;
import rs.raf.infview.model.type.ResourceType;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeObserverAdapter;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractFile;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.BulkReader;
import rs.raf.infview.util.io.file.FileOperator;
import rs.raf.infview.util.io.file.RecordAverager;
import rs.raf.infview.util.io.file.SequentialFile;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import rs.raf.infview.view.frame.ResultFrame;
import rs.raf.infview.view.frame.SortRecordFrame;

public class SortAction extends Command {

    private Entity entity;
    private ChangeObserver<Entity> callback;
    
    public SortAction(Entity entity, ChangeObserver<Entity> callback) {
        this.entity = entity;
        this.callback = callback;
    }

    @Override
    public void execute() {
    	if(((Resource) entity.getParent()).getType() == ResourceType.RELATIONAL_DATABASE) {
    		
    		Map<String, AbstractResource.SortType> map = new LinkedHashMap();
    		
    		new SortRecordFrame(entity, new Record(), new ChangeObserverAdapter<Record>() {
                @Override
                public void onChange(ChangeType type, Record bundle) {
                    if(type != ChangeType.APPLY) {
                        return;
                    }
                    
                    map.putAll(SortRecordFrame.sortInfo);
                    
                    new ResultFrame(entity, ((BulkReader) Context.instance()
                			.fileMatcher.get(entity))
                			.bulkSort(map)).open();
                }
            }).open();
    	}else {
    	
	    	AbstractFile file = (AbstractFile) Context.instance().fileMatcher.get(entity);
	    	Map<String, AbstractResource.SortType> map = new HashMap<>();
	    	FetchAction fetch = new FetchAction(entity, callback);
	    	fetch.setBlockSize(App.DEFAULT_BLOCK_SIZE);
	    	SortRecordFrame frame;
	    	
	    	if(file instanceof SequentialFile) {
	    		frame = new SortRecordFrame(entity, new Record(), new ChangeObserverAdapter<Record>() {
	                @Override
	                public void onChange(ChangeType type, Record bundle) {
	                    if(type != ChangeType.APPLY) {
	                        return;
	                    }
	                    
	                    map.putAll(SortRecordFrame.sortInfo);
	                    
	                    new FileOperator(file).sort(map);
	                    file.resetPointer();
	                    DialogAdapter.info(Res.STRINGS.FILE_SORTED);
	                    fetch.resetPointer(file);
	                }
	            });
	    		frame.open();
	    		return;
	    	}
	    	new FileOperator(file).sort(null);
	        file.resetPointer();
	        DialogAdapter.info(Res.STRINGS.FILE_SORTED);
	        fetch.resetPointer(file);
    	}
    }
}