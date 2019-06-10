package rs.raf.infview.util.io.file.converter;

import rs.raf.infview.core.Res;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.util.io.file.SequentialFile;
import rs.raf.infview.util.io.file.SerialFile;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import java.io.IOException;
import java.util.Set;

public class SequentialConverter implements FileConverter<SerialFile, SequentialFile> {

    @Override
    public SequentialFile convert(SerialFile file) {
        String path = file.getPath() + EXTENSION;
        Set<Attribute> attributes = file.getAttributes();

        try {
            SerialFile sorted = new SerialFile();
            sorted.open(path, true, attributes);

            file.copyTo(sorted);

            sorted.sort(null);
            sorted.close();

            SequentialFile sequential = new SequentialFile();
            sequential.open(path, false, attributes);

            return sequential;
        }catch(IOException e) {
            DialogAdapter.error(Res.STRINGS.ERROR_CANNOT_CONVERT);
            return null;
        }
    }
}