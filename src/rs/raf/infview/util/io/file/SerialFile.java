package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Record;
import rs.raf.infview.util.io.file.converter.SequentialConverter;
import rs.raf.infview.util.log.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SerialFile extends AbstractFile {

    @Override
    public boolean create(Record record) {
        if(file == null) {
            return false;
        }

        try {
            file.seek(file.length());
            file.writeBytes(NEW_LINE);
            file.writeBytes(record.serialize(attributes));
        }catch(IOException e) {
            return false;
        }finally {
            resetPointer();
        }

        return true;
    }

    @Override
    public boolean update(Record oldRecord, Record newRecord) {
        if(file == null) {
            return false;
        }

        resetPointer();

        try {
            long lastPosition = file.getFilePointer();
            Record current = read();

            while(current != null) {
                if(oldRecord.equals(current)) {
                    file.seek(lastPosition);
                    file.writeBytes(newRecord.serialize(attributes));
                    file.writeBytes(NEW_LINE);
                    break;
                }

                lastPosition = file.getFilePointer();
                current = read();
            }
        }catch(IOException e) {
            return false;
        }finally {
            resetPointer();
        }

        return true;
    }

    @Override
    public boolean delete(Record record) {
        if(file == null) {
            return false;
        }

        resetPointer();

        try {
            long lastPosition = file.getFilePointer();
            Record current = read();
            Record prev = null;

            while(current != null) {
                if(getPointer() == 0 && prev != null) {
                    break;
                }

                if(record.equals(current)) {
                    file.seek(lastPosition);
                    file.writeBytes(REMOVE_TOKEN);
                    break;
                }

                lastPosition = file.getFilePointer();
                prev = current;
                current = read();
            }
        }catch(IOException e) {
            return false;
        }finally {
            resetPointer();
        }

        return true;
    }

    @Override
    public List<Record> search(Record record, boolean single) {
        if(file == null) {
            return null;
        }

        resetPointer();

        List<Record> records = new LinkedList<>();

        Record current = read();
        Record prev = null;

        while(current != null) {
            if(getPointer() == 0 && prev != null) {
                break;
            }

            current.setAttributes(record.getAttributes());

            if(current.equals(record)) {
                records.add(current);

                if(single) {
                    break;
                }
            }

            prev = current;
            current = read();
        }

        resetPointer();

        return records;
    }

    @Override
    public void sort(Map<String, AbstractResource.SortType> map) {
        if(file == null) {
            return;
        }
        
        try {
        	long blockSize = 1024 * 1024;
			long size = getInfo().get(AbstractResource.InfoEntry.TOTAL_RECORDS);
			if(blockSize > size)
				blockSize = size;
			long numOfFIles = size / blockSize;
			ArrayList<String> files = new ArrayList<>();
			for(long i = 0; i < numOfFIles; i++) {
				
				StringBuilder name = new StringBuilder();
				name.append(getPath());
				name.append(i);
				SerialFile serial = new SerialFile();
				serial.open(name.toString(), true, getAttributes());
				files.add(name.toString());
				
				ArrayList<Record> sortedRecords = new ArrayList<>();
				for(int j = 0; j < blockSize; j++) {
					Record record = read();
					if(record == null) break;
					record.setAttributes(getAttributes());
					sortedRecords.add(record);
				}
				
				sortedRecords.sort(null);
				for(Record r : sortedRecords) {
					serial.create(r);
				}
				serial.close();
			}
			
			while(files.size() > 1) {
				for(int i = 0; i < files.size(); i += 2) {
					SerialFile serialLeft = new SerialFile();
					SerialFile serialRight = new SerialFile();
					serialLeft.open(files.get(i), false, getAttributes());
					if(i + 1 >= files.size()) {
						serialRight.open(files.get(i) + "t", true, getAttributes());
					}else {
						serialRight.open(files.get(i + 1), false, getAttributes());
						files.remove(i + 1);
					}
					merge(serialLeft, serialRight);
					serialLeft.close();
					serialRight.close();
				}
			}
			
			SerialFile finalFile = new SerialFile();
			finalFile.open(files.get(0), false, getAttributes());
			finalFile.copyTo(this);
			finalFile.close();
			
			File tmp = new File(files.get(0));
			tmp.delete();
		} catch (IOException e) {
			Log.e(e.getMessage());
        }finally {
			resetPointer();
		}
        
    }
    
    private void merge(SerialFile f1, SerialFile f2) {
    	SerialFile tmp = new SerialFile();
    	try {
			tmp.open(getPath() + "Tmp", true, getAttributes());
			Record r1 = f1.read();
			r1.setAttributes(getAttributes());
			Record r2 = f2.read();
			if(r2 != null)
				r2.setAttributes(getAttributes());
			
			if(r1 != null && r2 != null)
				do{
					if(r1.compareTo(r2) <= 0) {
						tmp.create(r1);
						r1 = f1.read();
						r1.setAttributes(getAttributes());
					}else { 
						tmp.create(r2);
						r2 = f2.read();
						r2.setAttributes(getAttributes());
					}
				}while(r1 != null && r2 != null && f1.getPointer() != 0 && f2.getPointer() != 0);
			if(r1 != null && f1.getPointer() != 0) {
				if(r2 != null)
					tmp.create(r2);
				do{
					tmp.create(r1);
					r1 = f1.read();
					r1.setAttributes(getAttributes());
				}while(r1 != null && f1.getPointer() != 0);
				if(r1 != null)
					tmp.create(r1);
			}else if(r2 != null && f2.getPointer() != 0) {
				if(r1 != null)
					tmp.create(r1);
				do{
					tmp.create(r2);
					r2 = f2.read();
					r2.setAttributes(getAttributes());
				}while(r2 != null && f2.getPointer() != 0);
				if(r2 != null)
					tmp.create(r2);
			}
			
			f1.open(f1.getPath(), true, getAttributes());
			tmp.resetPointer();
			tmp.copyTo(f1);
			
			tmp.close();
			File tmpFile = new File(tmp.getPath());
			tmpFile.delete();
		} catch (IOException e) {
        }finally {
			f1.resetPointer();
			f1.close();
			f2.close();
			File f2File = new File(f2.getPath());
			f2File.delete();
		}
    }

    @Override
    public AbstractResource convert() {
        return new SequentialConverter().convert(this);
    }
}