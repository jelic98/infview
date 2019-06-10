package rs.raf.infview.util.io.file;

import rs.raf.infview.core.App;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.converter.tree.BlockTree;
import rs.raf.infview.util.io.file.converter.tree.BlockTreeMaker;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class IndexFile extends AbstractFile {

    @Override
    public boolean create(Record record) {
        if(file == null) {
            return false;
        }

        return false;
    }

    @Override
    public boolean update(Record oldRecord, Record newRecord) {
        if(file == null) {
            return false;
        }

        return false;
    }

    @Override
    public boolean delete(Record record) {
        if(file == null) {
            return false;
        }

        return false;
    }

    @Override
    public List<Record> search(Record record, boolean single) {
    	 if(file == null) {
             return null;
         }

         List<Record> records = new LinkedList<>();
         
         SequentialFile sequential = new SequentialFile();
         
         try {
         	sequential.open(path, false, attributes);
         }catch(IOException e) {
         	return records;
         }
         
         String[] primaries = record.getPrimary().split(Record.PRIMARY_PARSER);

     	int prevCmp = 0;
     	long prevPtr = 0;
         
         BlockTreeMaker maker = new BlockTreeMaker();
         BlockTree tree = maker.makeTree(sequential, App.DEFAULT_BLOCK_SIZE);
         
         List<List<String>> blocks = tree.getBlocks();
         
         for(List<String> block : blocks) {
         	if(primaries.length != block.size() - 1) {
         		continue;
         	}
         	
         	boolean found = true;
         	
         	int currCmp = 0;
     		long currPtr = Long.parseLong(block.get(block.size() - 1));
     		
         	
         	for(int i = 0; i < primaries.length; i++) {
         		currCmp = primaries[i].compareTo(block.get(i));
         		
         		if(currCmp != 0) {
         			found = false;
         			break;
         		}
             }
         	
         	if(found) {
         		setPointer(currPtr);
         	}else {
         		if(prevCmp < 0 && currCmp > 0) {
         			setPointer(prevPtr);
         		}else if(currCmp < 0)
         			break;
         	}
         	
         	prevCmp = currCmp;
         	prevPtr = currPtr;

         	Record current = read();
         	
             while(current != null) {
                 boolean found2 = true;

                 for(Attribute a : attributes) {
                     String key = a.getName();
                     String criteria = record.get(key);
                     String value = current.get(key);

                     if(criteria != null && !criteria.isEmpty() && !criteria.equals(value)) {
                         found2 = false;
                     }
                 }

                 if(found2) {
                     records.add(current);
                     
                     if(single) {
                         break;
                     }
                 }
                 
                 if(record.compareTo(current) < 0)
                 	break;
                 
                 current = read();
             }
             
         }
         resetPointer();
         return records;
    }

    @Override
    public void sort(Map<String, AbstractResource.SortType> map) {
        if(file == null) {
            return;
        }
    }

    @Override
    public AbstractResource convert() {
        return this;
    }
}