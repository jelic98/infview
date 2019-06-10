package rs.raf.infview.util.io.file.converter;

import rs.raf.infview.core.App;
import rs.raf.infview.core.Res;
import rs.raf.infview.model.Attribute;
import rs.raf.infview.util.io.file.IndexFile;
import rs.raf.infview.util.io.file.SequentialFile;
import rs.raf.infview.util.io.file.converter.tree.BlockTree;
import rs.raf.infview.util.io.file.converter.tree.BlockTreeMaker;
import rs.raf.infview.view.adapter.dialog.DialogAdapter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class IndexConverter implements FileConverter<SequentialFile, IndexFile> {

    @Override
    public IndexFile convert(SequentialFile file) {
        BlockTreeMaker maker = new BlockTreeMaker();
        BlockTree tree = maker.makeTree(file, App.DEFAULT_BLOCK_SIZE);

        List<List<String>> blocks = tree.getBlocks();

        String path = file.getPath() + EXTENSION;
        Set<Attribute> attributes = file.getAttributes();

        try {
            IndexFile index = new IndexFile();
            index.open(path, true, attributes);

            for(List<String> block : blocks) {
                index.append(block);
            }

            return index;
        }catch(IOException e) {
            DialogAdapter.error(Res.STRINGS.ERROR_CANNOT_CONVERT);
            return null;
        }
    }
}