package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.comparator.SortRecordComparator;
import rs.raf.infview.util.io.file.converter.IndexConverter;
import rs.raf.infview.util.log.Log;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SequentialFile extends AbstractFile {

    @Override
    public boolean create(Record record) {
        return file != null;
    }

    @Override
    public boolean update(Record oldRecord, Record newRecord) {
        if(file == null) {
            return false;
        }

        resetPointer();

        try {
            long pos = binarySearch(oldRecord);
            
        	if(pos >= 0) {
        		file.seek(pos);
        		file.writeBytes(newRecord.serialize(attributes));
                file.writeBytes(NEW_LINE);
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

        return false;
    }

    @Override
    public List<Record> search(Record record, boolean single) {
        if(file == null) {
            return null;
        }

        if(searchType != SearchType.LINEAR_SEARCH_FROM_CURRENT)
        	resetPointer();

        List<Record> records = new LinkedList<>();

        try {
            if(searchType == SearchType.BINARY_SEARCH) {
            	long pos = binarySearch(record);
            	
            	if(pos >= 0) {
            		file.seek(pos);
                    records.add(read());
            	}
            }else {
            	Record current = read();

                while(current != null) {
                    boolean found = true;

                    for(Attribute a : attributes) {
                        String key = a.getName();
                        String criteria = record.get(key);
                        String value = current.get(key);

                        if(criteria != null && !criteria.isEmpty() && !criteria.equals(value)) {
                            found = false;
                            break;
                        }
                    }

                    if(found) {
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
        }catch(IOException e) {
            records.clear();
        }finally {
            resetPointer();
        }

        return records;
    }

    @Override
    public void sort(Map<String, AbstractResource.SortType> map) {
        if(file == null) {
            return;
        }
        
        try {
        	long blockSize = 1024 * 200;
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
				
				sortedRecords.sort(new SortRecordComparator(map));
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
					merge(serialLeft, serialRight, map);
					serialLeft.close();
					serialRight.close();
					File f2File = new File(serialRight.getPath());
					f2File.delete();
				}
			}
		} catch (IOException e) {
			Log.e("unsuccesfull");
        }finally {
			resetPointer();
		}
        
    }
    
    private void merge(SerialFile f1, SerialFile f2, Map<String, AbstractResource.SortType> map) {
    	SerialFile tmp = new SerialFile();
    	SortRecordComparator comparator = new SortRecordComparator(map);
    	try {
			tmp.open(getPath() + "Tmp", true, getAttributes());
			Record r1 = f1.read();
			r1.setAttributes(getAttributes());
			Record r2 = f2.read();
			if(r2 != null)
				r2.setAttributes(getAttributes());
			
			if(r1 != null && r2 != null)
				do{
					if(comparator.compare(r1, r2) <= 0) {
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
		}
    }

    @Override
    public AbstractResource convert() {
        return new IndexConverter().convert(this);
    }

    private long binarySearch(Record record) throws IOException {
        long[] pos = getRecordPositions();

        int left = 0;
        int right = pos.length;

        while(left <= right) {
            int mid = (int) (left + (right - left) * 0.5);

            file.seek(pos[mid]);

            Record curr = read();

            int compare = record.compareTo(curr);

            if(compare == 0) {
                return pos[mid];
            }else if(compare > 0) {
                left = mid + 1;
            }else {
                right = mid - 1;
            }
        }

        return -1;
    }
}