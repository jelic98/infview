package rs.raf.infview.util.io.file;

import rs.raf.infview.model.Attribute;
import rs.raf.infview.model.Record;
import rs.raf.infview.model.options.FileOptions;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Set;

public abstract class AbstractFile extends AbstractResource<FileOptions> {

    public static final String SEPARATOR = " ";
    static final String NEW_LINE = "\r\n";
    static final String REMOVE_TOKEN = "<<REMOVED>>";

    protected String path;
    protected RandomAccessFile file;
    private long readPointer;

    public void open(String path, boolean create, Set<Attribute> attributes) throws IOException {
        super.open(getEntity(path), attributes, new FileOptions(path));

        File file = new File(options.getPath());

        if(create) {
            if(file.exists()) {
                file.delete();
            }

            file.getParentFile().mkdirs();
            file.createNewFile();
        }

        if(!file.exists()) {
            throw new FileNotFoundException();
        }

        this.readPointer = 0;
        this.searchType = SearchType.LINEAR_SEARCH;
        this.file = new RandomAccessFile(file, "rw");
    }

    @Override
    public void close() {
        if(file == null) {
            return;
        }

        try {
            file.close();
        }catch(IOException e) {
            file = null;
        }
    }

    public void clear() {
        if(file == null) {
            return;
        }

        try {
            file.setLength(0);
        }catch(IOException e) {
            file = null;
        }
    }

    public void copyTo(AbstractFile dest) {
        if(file == null || dest.file == null) {
            return;
        }

        resetPointer();
        dest.resetPointer();

        dest.clear();

        try {
            for(int i = 0; i < file.length(); i++){
                dest.file.write(file.read());
            }
        }catch(IOException e) {
            dest.clear();
        }finally {
            resetPointer();
            dest.resetPointer();
        }
    }

    public long getPointer() {
        try {
            return file.getFilePointer();
        }catch(IOException e) {
            return 0;
        }
    }

    public void setPointer(long pointer) {
        try {
            file.seek(pointer);
            readPointer = pointer;
        }catch(IOException e) {
            file = null;
        }
    }

    public void resetPointer() {
        setPointer(0);
    }

    public boolean append(List<String> tokens) {
        if(file == null) {
            return false;
        }

        try {
            file.seek(file.length());

            for(int i = 0; i < tokens.size(); i++) {
                if(i > 0) {
                    file.writeBytes(SEPARATOR);
                }

                file.writeBytes(tokens.get(i));
            }

            file.writeBytes(NEW_LINE);
        }catch(IOException e) {
            return false;
        }finally {
            resetPointer();
        }

        return true;
    }

    private void skipSpecial() {
        try {
            byte curr;

            do {
                curr = file.readByte();
            }while(isSpecialChar(curr));

            file.seek(file.getFilePointer() - 1);
        }catch(IOException e) {
            resetPointer();
        }
    }

    private boolean isSpecialChar(byte c) {
        return c < 26 || c > 126;
    }

    public Record read() {
        if(file == null) {
            return null;
        }

        Record record = new Record();

        try {
            file.seek(readPointer);

            StringBuilder builder = new StringBuilder();

            for(Attribute a : attributes) {
                for(int i = 0; i < a.getLength(); i++) {
                    builder.append((char) file.readByte());
                }

                record.set(a.getName(), builder.toString().trim());

                builder.setLength(0);
            }

            skipSpecial();

            readPointer = file.getFilePointer();

            if(record.serialize(attributes).startsWith(REMOVE_TOKEN)) {
                return read();
            }
        }catch(IOException | NullPointerException e) {
            return null;
        }

        return record;
    }
    
    long[] getRecordPositions() {
        if(file == null) {
            return new long[0];
        }

        resetPointer();

        long[] pos = new long[getInfo().get(InfoEntry.TOTAL_RECORDS)];

        try {
            int i = 0;

            String line = file.readLine();

            while(line != null) {
                pos[i++] = file.getFilePointer();

                line = file.readLine();
            }
        }catch(IOException e) {
            return new long[0];
        }finally {
            resetPointer();
        }

        return pos;
    }

    protected int getTotalRecords() {
        if(file == null) {
            return 0;
        }

        resetPointer();

        try {
            return (int) (file.length() / getRecordSize());
        }catch(Exception e) {
            return 0;
        }finally {
            resetPointer();
        }
    }

    protected int getRecordSize() {
        if(file == null) {
            return 0;
        }

        resetPointer();

        try {
            return file.readLine().length();
        }catch(Exception e) {
            return 0;
        }finally {
            resetPointer();
        }
    }

    public String getPath() {
        return path;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    private String getEntity(String path) {
        String file = new File(path).getName();
        return file.substring(0, file.lastIndexOf('.'));
    }
}