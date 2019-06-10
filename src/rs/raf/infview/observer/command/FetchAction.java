package rs.raf.infview.observer.command;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import rs.raf.infview.model.Entity;
import rs.raf.infview.model.Record;
import rs.raf.infview.observer.ChangeObserver;
import rs.raf.infview.observer.ChangeType;
import rs.raf.infview.state.Context;
import rs.raf.infview.util.io.file.AbstractFile;
import rs.raf.infview.util.io.file.AbstractResource;
import rs.raf.infview.util.io.file.BulkReader;
import rs.raf.infview.util.io.file.FileOperator;

public class FetchAction extends Command{

	private static Map<AbstractResource, Long> filePointers = new HashMap<>();

	private int blockSize;
	private Entity entity;
	private ChangeObserver<Entity> callback;

	public FetchAction(Entity entity, ChangeObserver<Entity> callback) {
		this.entity = entity;
		this.callback = callback;
	}

	@Override
	public void execute() {
		AbstractResource resource = Context.instance().fileMatcher.get(entity);
		
		if(resource == null) {
			return;
		}

		Set<Record> records = new TreeSet<>();

		FileOperator operator = new FileOperator(resource);

		if(resource instanceof AbstractFile) {
			AbstractFile file = (AbstractFile) resource;
			file.setPointer(getCurrentPointer(file));

			for(int i = 0; i < blockSize; i++) {
				Record record = operator.read();

				if(record == null) {
					break;
				}

				record.setParent(entity);

				records.add(record);
			}

			if(!records.isEmpty()) {
				filePointers.put(file, file.getPointer());
			}
		}else {
			records.addAll(operator.bulkRead());
		}

		if(!records.isEmpty()) {
			entity.setRecords(records);
			callback.onChange(ChangeType.REFRESH, entity);
		}
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public void setEntity(Entity entity) {
		this.entity = entity;
	}

	public int getBlockSize() {
		return blockSize;
	}
	
	public long getCurrentPointer(AbstractResource file) {
		if(filePointers.get(file) == null)
			return 0;

		return filePointers.get(file);
	}
	
	public void resetPointer(AbstractResource file){
		if(!(filePointers == null || filePointers.get(file) == null || filePointers.get(file) == 0)) {
			filePointers.put(file, (long) 0);
		}
		execute();
	}
	
}
